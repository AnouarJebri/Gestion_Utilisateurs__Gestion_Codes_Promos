<?php

namespace App\Controller;

use App\Entity\Utilisateur;
use App\Form\RegistrationFormType;
use App\Security\EmailVerifier;
use App\Security\UtilisateurAuthenticator;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mailer\Mailer;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mailer\Transport;
use Symfony\Component\Mime\Address;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Security\Http\Authentication\UserAuthenticatorInterface;
use Symfony\Contracts\Translation\TranslatorInterface;
use SymfonyCasts\Bundle\VerifyEmail\Exception\VerifyEmailExceptionInterface;
use Symfony\Component\Mime\Email;
use SymfonyCasts\Bundle\VerifyEmail\VerifyEmailHelperInterface;

class RegistrationController extends AbstractController
{
    private EmailVerifier $emailVerifier;
    private VerifyEmailHelperInterface $verifyEmailHelper;

    public function __construct(EmailVerifier $emailVerifier, VerifyEmailHelperInterface $verifyEmailHelper)
    {
        $this->emailVerifier = $emailVerifier;
        $this->verifyEmailHelper = $verifyEmailHelper;
    }

    private function generateVerificationLink(UserInterface $user): string
    {
        // Use the VerifyEmailHelperInterface service to generate the verification link
        return $this->verifyEmailHelper->generateSignature(
            'app_verify_email', // Route name for email verification
            $user->getId(),
            $user->getEmail()
        )->getSignedUrl();
    }


    #[Route('/register', name: 'app_register')]
    public function register(Request $request, UserPasswordHasherInterface $userPasswordHasher, UserAuthenticatorInterface $userAuthenticator, UtilisateurAuthenticator $authenticator, EntityManagerInterface $entityManager): Response
    {
        $transport = Transport::fromDsn('smtp://anouar.jebri@gmail.com:umqvgleqwbbekqrd@smtp.gmail.com:587');
        $mailer = new Mailer($transport);
        $user = new Utilisateur();
        $form = $this->createForm(RegistrationFormType::class, $user);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // encode the plain password
            $user->setPassword(
                $userPasswordHasher->hashPassword(
                    $user,
                    $form->get('plainPassword')->getData()
                )
            );
            $roles[]='ROLE_USER';
            $user->setRoles($roles);
            $entityManager->persist($user);
            $entityManager->flush();
            /* $email = (new TemplatedEmail())
                 ->from(new Address('anouar.jebri@gmail.com', 'studentors'))
                 ->to($user->getEmail())
                 ->subject('Please Confirm your Email');*/
            /*$expirationData = [
                'days' => '7',
                'hours' => '12'
            ];*/
            // generate a signed url and email it to the user
            $this->emailVerifier->sendEmailConfirmation('app_verify_email', $user);//,$email);
            //$this->emailVerifier->sendConfirmationEmail($user->getEmail(),$this->generateVerificationLink($user),'email_verification.expiration',json_encode($expirationData));
            // do anything else you need here, like send an email
            $this->addFlash('success', 'An email confirmation has been sent. Please check your email.');
            /* $mailer->send((new Email())
                 ->from('anouar.jebri@gmail.com')
                 ->to($user->getEmail())
                 ->subject('Hello from Studentors')
                 ->text('Hello, you have been successfully subscribed.')
             );*/
            if($user->isVerified()){
                return $userAuthenticator->authenticateUser(
                    $user,
                    $authenticator,
                    $request
                );
            }
            else{
                echo "<script>alert(\"Please check you email to verify your account!\");</script>";
            }
        }

        return $this->render('registration/register.html.twig', [
            'registrationForm' => $form->createView(),
        ]);
    }

    #[Route('/verify/email', name: 'app_verify_email')]
    public function verifyUserEmail(Request $request, TranslatorInterface $translator): Response
    {
        $this->denyAccessUnlessGranted('IS_AUTHENTICATED_FULLY');

        // validate email confirmation link, sets User::isVerified=true and persists
        try {
            $this->emailVerifier->handleEmailConfirmation($request, $this->getUser());
        } catch (VerifyEmailExceptionInterface $exception) {
            $this->addFlash('verify_email_error', $translator->trans($exception->getReason(), [], 'VerifyEmailBundle'));

            return $this->redirectToRoute('app_login');
        }

        // @TODO Change the redirect on success and handle or remove the flash message in your templates
        $this->addFlash('success', 'Your email address has been verified.');

        return $this->redirectToRoute('home');
    }
}
