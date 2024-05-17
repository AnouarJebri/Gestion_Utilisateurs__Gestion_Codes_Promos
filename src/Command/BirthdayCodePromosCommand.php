<?php

namespace App\Command;

use App\Entity\CodePromos;
use Symfony\Component\Console\Attribute\AsCommand;
use Symfony\Component\Console\Command\Command;
use App\Entity\Utilisateur;
use App\Repository\UtilisateurRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\Mailer\Exception\TransportExceptionInterface;
use Symfony\Component\Mailer\Mailer;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mailer\Transport;
use Symfony\Component\Mime\Address;
use Symfony\Component\Mime\Email;

#[AsCommand(name: 'app:generate-code-promos')]
class BirthdayCodePromosCommand extends Command
{
    protected static $defaultName = 'app:generate-code-promos';
    private EntityManagerInterface $entityManager;
    private UtilisateurRepository $userRepository;
    private MailerInterface $mailer;

    public function __construct(EntityManagerInterface $entityManager, UtilisateurRepository $userRepository)
    {
        parent::__construct();
        $this->entityManager = $entityManager;
        $this->userRepository = $userRepository;
        $transport = Transport::fromDsn('smtp://anouar.jebri@gmail.com:umqvgleqwbbekqrd@smtp.gmail.com:587');
        $this->mailer = new Mailer($transport);
    }

    protected function configure()
    {
        $this->setDescription('Generates CodePromos for Utilisateur with birthdays today');
    }
    function generateUniquePromoCode($entityManager):string
    {
        $characters = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $length = 8;
        do {
            $promoCode = '';

            // Generate random characters
            for ($i = 0; $i < $length; $i++) {
                $promoCode .= $characters[rand(0, strlen($characters) - 1)];
            }

            // Check if the generated code already exists in the database
            $existingCode = $entityManager->getRepository(CodePromos::class)->findOneBy(['code' => $promoCode]);
        } while ($existingCode !== null);

        // Return the unique promo code
        return $promoCode;
    }


    protected function execute(InputInterface $input, OutputInterface $output):int
    {
        $today = new \DateTime();
        $utilisateur = $this->userRepository->findByBirthDay($today);
        if($utilisateur==null){
            $output->writeln('No users');
        }
        else {
            foreach ($utilisateur as $utilisateur) {
                $codePromos = new CodePromos();
                $expirationDate = new \DateTime();
                $expirationDate->add(new \DateInterval('P3D')); // Add 3 days
                $codePromos->setDateExpiration($expirationDate);
                $uniqueCode = $this->generateUniquePromoCode($this->entityManager);
                $codePromos->setCode($uniqueCode); // Generate a unique code, you may adjust this logic

                $utilisateur->addIdCp($codePromos);
                $this->entityManager->persist($codePromos);
                $email = (new Email())
                    ->from(new Address('anouar.jebri@gmail.com', 'studentors'))
                    ->to($utilisateur->getEmail())
                    ->subject('CodePromos Generated')
                    ->html('<h1>Hello, dear User!</h1>
                <p>Happy Birthday! You have a CodePromos generated specially for you!</p>
                <p>this code will expire in 3 days </p>
                 <p><strong>' . $uniqueCode . '</strong></p>
                <p><strong>Enjoy your special offer,Happy studying!</strong></p>');

                $this->mailer->send($email);
                $output->writeln('User Found: ' . $utilisateur->getUsername());
            }
            $this->entityManager->flush();
        }
        //$this->entityManager->flush();
        //$output->writeln('CodePromos generated for Utilisateur with birthdays today.');

        return Command::SUCCESS;
    }
}