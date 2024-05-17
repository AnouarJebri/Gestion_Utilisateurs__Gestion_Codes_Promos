<?php

namespace App\Controller;

use App\Entity\Utilisateur;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;

class HashController extends AbstractController
{
    private $passwordHasher;

    public function __construct(UserPasswordHasherInterface $passwordHasher)
    {
        $this->passwordHasher = $passwordHasher;
    }

    #[Route('/hash', name: 'app_hash', methods: ['POST'])]
    public function index(Request $request): Response
    {
        $content = $request->getContent();

        // Decode the JSON payload
        $data = json_decode($content, true);

        // Get the password from the payload
        $password = $data['password'];

        // Hash the password using Symfony's default password hashing
        $hashedPassword = password_hash($password, PASSWORD_DEFAULT);

        // Return the hashed password as a response
        return new Response($hashedPassword);
    }
}
