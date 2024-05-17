<?php

namespace App\Security;

use Doctrine\ORM\EntityManagerInterface;
//use PharIo\Manifest\Email;
use Symfony\Bridge\Twig\Mime\BodyRenderer;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Mailer\Mailer;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mailer\Transport;
use Symfony\Component\Mime\Address;
use Symfony\Component\Security\Core\User\UserInterface;
use SymfonyCasts\Bundle\VerifyEmail\Exception\VerifyEmailExceptionInterface;
use SymfonyCasts\Bundle\VerifyEmail\VerifyEmailHelperInterface;
use Twig\Environment;
use Twig\Loader\FilesystemLoader;
use Symfony\Component\Mime\Email;

class EmailVerifier
{
    public function __construct(
        private VerifyEmailHelperInterface $verifyEmailHelper,
        private MailerInterface $mailer,
        private EntityManagerInterface $entityManager
    ) {
        $transport = Transport::fromDsn('smtp://anouar.jebri@gmail.com:umqvgleqwbbekqrd@smtp.gmail.com:587');
        $this->mailer=new Mailer($transport);
    }
    /*public function sendConfirmationEmail(string $toEmail, string $signedUrl, string $expiresAtMessageKey, string $expiresAtMessageData): void
    {
        $email = (new TemplatedEmail())
            ->from(new Address('anouar.jebri@gmail.com', 'studentors'))
            ->to($toEmail)
            ->subject('Please Confirm your Email')
            ->htmlTemplate('registration/confirmation_email.html.twig')
            ->context([
                'signedUrl' => $signedUrl,
                'expiresAtMessageKey' => $expiresAtMessageKey,
                'expiresAtMessageData' => $expiresAtMessageData,
            ]);
        $email->text("Welcome!!");

        $this->mailer->send($email);
    }*/

    public function sendEmailConfirmation(string $verifyEmailRouteName, UserInterface $user): void
    {
        $signatureComponents = $this->verifyEmailHelper->generateSignature(
            $verifyEmailRouteName,
            $user->getId(),
            $user->getEmail()
        );
        // Load Twig templates
        // Calculate the path to the templates directory
        $templatesPath = realpath(__DIR__ . '/../../templates');

        // Load Twig templates
        $loader = new FilesystemLoader(['__main__' => $templatesPath]);
        $twigEnv = new Environment($loader);
        // Create a new instance of BodyRenderer
        $twigBodyRenderer = new BodyRenderer($twigEnv);

        // Render HTML content
        $htmlContent = $twigEnv->render('registration/confirmation_email.html.twig', [
            'signedUrl' => $signatureComponents->getSignedUrl(),
            'expiresAtMessageKey' => $signatureComponents->getExpirationMessageKey(),
            'expiresAtMessageData' => $signatureComponents->getExpirationMessageData(),
        ]);
        // Render text content (assuming it's the same as HTML)
        $textContent = $twigEnv->render('registration/confirmation_email.html.twig', [
            'signedUrl' => $signatureComponents->getSignedUrl(),
            'expiresAtMessageKey' => $signatureComponents->getExpirationMessageKey(),
            'expiresAtMessageData' => $signatureComponents->getExpirationMessageData(),
        ]);

        $email = new Email();
        $email->from(new Address('anouar.jebri@gmail.com', 'studentors'));
        $email->to($user->getEmail());
        $email->subject('Please confirm your Email');
        $email->html($htmlContent);
        $email->text($textContent);
       /* $email = (new TemplatedEmail());
        $context = $email->getContext();
        $context['signedUrl'] = $signatureComponents->getSignedUrl();
        $context['expiresAtMessageKey'] = $signatureComponents->getExpirationMessageKey();
        $context['expiresAtMessageData'] = $signatureComponents->getExpirationMessageData();

        $email->from(new Address('anouar.jebri@gmail.com', 'studentors'));
        $email->to($user->getEmail());
        $email->subject("Please confirm your Email");
        $email->context($context);
        $email->htmlTemplate('registration/confirmation_email.html.twig');
        //$email->text('This is a plain text email.');
        $email->textTemplate('registration/confirmation_email.html.twig');*/
        $this->mailer->send($email);
    }

    /**
     * @throws VerifyEmailExceptionInterface
     */

    public function handleEmailConfirmation(Request $request, UserInterface $user): void
    {
        $this->verifyEmailHelper->validateEmailConfirmation($request->getUri(), $user->getId(), $user->getEmail());

        $user->setIsVerified(true);

        $this->entityManager->persist($user);
        $this->entityManager->flush();
    }
}
