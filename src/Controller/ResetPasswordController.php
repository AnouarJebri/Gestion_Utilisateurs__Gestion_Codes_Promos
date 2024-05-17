<?php

namespace App\Controller;

use App\Entity\Utilisateur;
use App\Form\ChangePasswordFormType;
use App\Form\ResetPasswordRequestFormType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bridge\Twig\Mime\BodyRenderer;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mailer\Mailer;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mailer\Transport;
use Symfony\Component\Mime\Address;
use Symfony\Component\Mime\Email;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use Symfony\Contracts\Translation\TranslatorInterface;
use SymfonyCasts\Bundle\ResetPassword\Controller\ResetPasswordControllerTrait;
use SymfonyCasts\Bundle\ResetPassword\Exception\ResetPasswordExceptionInterface;
use SymfonyCasts\Bundle\ResetPassword\ResetPasswordHelperInterface;
use Twig\Environment;
use Twig\Loader\FilesystemLoader;

#[Route('/reset-password')]
class ResetPasswordController extends AbstractController
{
    use ResetPasswordControllerTrait;
    private Mailer $mailer;
    private ResetPasswordHelperInterface $resetPasswordHelper;
    private EntityManagerInterface $entityManager;

    public function __construct(
        ResetPasswordHelperInterface $resetPasswordHelper,
        EntityManagerInterface $entityManager,
        MailerInterface $mailer
    ) {
        $this->resetPasswordHelper = $resetPasswordHelper;
        $this->entityManager = $entityManager;
        $transport = Transport::fromDsn('smtp://anouar.jebri@gmail.com:umqvgleqwbbekqrd@smtp.gmail.com:587');
        $this->mailer = new Mailer($transport);
    }

    /**
     * Display & process form to request a password reset.
     */
    #[Route('', name: 'app_forgot_password_request')]
    public function request(Request $request,TranslatorInterface $translator): Response
    {
        $form = $this->createForm(ResetPasswordRequestFormType::class);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            /*$transport = Transport::fromDsn('smtp://anouar.jebri@gmail.com:umqvgleqwbbekqrd@smtp.gmail.com:587');
            $mailer = new Mailer($transport);*/
            /*$email = (new Email())
                ->from(new Address('anouar.jebri@gmail.com', 'studentors'))
                ->to($form->get('email')->getData())
                ->subject('Your password reset request')
                ->html('<h1>Hi!</h1>
                    <p>To reset your password, please visit the following link</p>

                    <p>Cheers!</p>');
            $this->mailer->send($email);*/
            return $this->processSendingPasswordResetEmail(
                $form->get('email')->getData(),
                $translator,
                $form
            );
        }

        return $this->render('reset_password/request.html.twig', [
            'requestForm' => $form->createView(),
        ]);
    }

    /**
     * Confirmation page after a user has requested a password reset.
     */
    #[Route('/check-email', name: 'app_check_email')]
    public function checkEmail(): Response
    {
        // Generate a fake token if the user does not exist or someone hit this page directly.
        // This prevents exposing whether or not a user was found with the given email address or not
        if (null === ($resetToken = $this->getTokenObjectFromSession())) {
            $resetToken = $this->resetPasswordHelper->generateFakeResetToken();
        }

        return $this->render('reset_password/check_email.html.twig', [
            'resetToken' => $resetToken,
        ]);
    }

    /**
     * Validates and process the reset URL that the user clicked in their email.
     */
    #[Route('/reset/{token}', name: 'app_reset_password')]
    public function reset(Request $request, UserPasswordHasherInterface $passwordHasher, TranslatorInterface $translator, string $token = null): Response
    {
        if ($token) {
            // We store the token in session and remove it from the URL, to avoid the URL being
            // loaded in a browser and potentially leaking the token to 3rd party JavaScript.
            $this->storeTokenInSession($token);

            return $this->redirectToRoute('app_reset_password');
        }

        $token = $this->getTokenFromSession();
        if (null === $token) {
            throw $this->createNotFoundException('No reset password token found in the URL or in the session.');
        }

        try {
            $user = $this->resetPasswordHelper->validateTokenAndFetchUser($token);
        } catch (ResetPasswordExceptionInterface $e) {
            $this->addFlash('reset_password_error', sprintf(
                '%s - %s',
                $translator->trans(ResetPasswordExceptionInterface::MESSAGE_PROBLEM_VALIDATE, [], 'ResetPasswordBundle'),
                $translator->trans($e->getReason(), [], 'ResetPasswordBundle')
            ));

            return $this->redirectToRoute('app_forgot_password_request');
        }

        // The token is valid; allow the user to change their password.
        $form = $this->createForm(ChangePasswordFormType::class);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // A password reset token should be used only once, remove it.
            $this->resetPasswordHelper->removeResetRequest($token);

            // Encode(hash) the plain password, and set it.
            $encodedPassword = $passwordHasher->hashPassword(
                $user,
                $form->get('plainPassword')->getData()
            );

            $user->setPassword($encodedPassword);
            $this->entityManager->flush();

            // The session is cleaned up after the password has been changed.
            $this->cleanSessionAfterReset();

            return $this->redirectToRoute('home');
        }

        return $this->render('reset_password/reset.html.twig', [
            'resetForm' => $form->createView(),
        ]);
    }

    private function processSendingPasswordResetEmail(string $emailFormData,TranslatorInterface $translator,$form): RedirectResponse
    {
        $user = $this->entityManager->getRepository(Utilisateur::class)->findOneBy([
            'email' => $form->get('email')->getData(),
        ]);
        // Do not reveal whether a user account was found or not.
        if (!$user) {
            return $this->redirectToRoute('app_check_email');
        }


            $resetToken = $this->resetPasswordHelper->generateResetToken($user);

        $templatesPath = realpath(__DIR__ . '/../../templates');

        // Load Twig templates
        $loader = new FilesystemLoader(['__main__' => $templatesPath]);
        $twigEnv = new Environment($loader);
        $htmlContent = $this->renderView('reset_password/email.html.twig', [
            'resetToken' => $resetToken,
        ]);
       // $resetUrl = $this->generateUrl('app_reset_password', ['token' => $resetToken->getToken()], UrlGeneratorInterface::ABSOLUTE_URL);

        $email = (new Email())
            ->from(new Address('anouar.jebri@gmail.com', 'studentors'))
            ->to($form->get('email')->getData())
            ->subject('Your password reset request')
            ->html($htmlContent);
        $this->mailer->send($email);
        // Store the token object in session for retrieval in check-email route.
        $this->setTokenObjectInSession($resetToken);

        return $this->redirectToRoute('app_check_email');
    }
}
