<?php

namespace App\Controller;

use App\Entity\Utilisateur;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;
use Symfony\Component\Security\Core\User\UserInterface;

;

class HashPasswordController extends AbstractController
{
    private $passwordEncoder;

    public function __construct(UserPasswordEncoderInterface $passwordEncoder)
    {
        $this->passwordEncoder = $passwordEncoder;
    }
    #[Route('/hash/password', name: 'app_hash_password')]
    public function index(Request $request): Response
    {
        $requestData = json_decode($request->getContent(), true);

        // Assuming the request contains 'email' and 'password' fields
        $email = $requestData['email'];
        $password = $requestData['password'];

        // Fetch the user from your database based on the provided email
        $user = $this->getDoctrine()->getRepository(Utilisateur::class)->findOneBy(['email' => $email]);

        if (!$user) {
            return new Response('User not found', Response::HTTP_NOT_FOUND);
        }

        // Check if the entered password is correct
        if ($this->passwordEncoder->isPasswordValid($user, $password)) {
            return new Response('Credentials are correct', Response::HTTP_OK);
        }

        return new Response('Invalid credentials', Response::HTTP_UNAUTHORIZED);
    }
}
