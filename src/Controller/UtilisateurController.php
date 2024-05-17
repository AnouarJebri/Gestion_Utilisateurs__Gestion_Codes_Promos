<?php

namespace App\Controller;

use App\Entity\Utilisateur;
use App\Form\UtilisateurType;
use App\Repository\UtilisateurRepository;
use Doctrine\ORM\EntityManagerInterface;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\IsGranted;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/utilisateur')]
class UtilisateurController extends AbstractController
{
    #[Route('/', name: 'app_utilisateur_index', methods: ['GET'])]
    #[IsGranted("ROLE_ADMIN")]
    public function index(UtilisateurRepository $utilisateurRepository): Response
    {
        return $this->render('utilisateur/index.html.twig', [
            'utilisateur' => $utilisateurRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_utilisateur_new', methods: ['GET', 'POST'])]
    #[IsGranted("ROLE_ADMIN")]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $utilisateur = new Utilisateur();
        $form = $this->createForm(UtilisateurType::class, $utilisateur);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($utilisateur);
            $entityManager->flush();

            return $this->redirectToRoute('app_utilisateur_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('utilisateur/new.html.twig', [
            'utilisateur' => $utilisateur,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_utilisateur_show', methods: ['GET'])]
    public function show(Utilisateur $utilisateur): Response
    {
        return $this->render('utilisateur/show.html.twig', [
            'utilisateur' => $utilisateur,
        ]);
    }
    #[Route('/profile/{id}', name: 'app_utilisateur_showmyprofile', methods: ['GET'])]
    public function showmp(Utilisateur $utilisateur): Response
    {
        return $this->render('utilisateur/show_profile.html.twig', [
            'utilisateur' => $utilisateur,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_utilisateur_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Utilisateur $utilisateur, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(UtilisateurType::class, $utilisateur);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('home', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('utilisateur/edit.html.twig', [
            'utilisateur' => $utilisateur,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_utilisateur_delete', methods: ['POST'])]
    public function delete(Request $request, Utilisateur $utilisateur, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$utilisateur->getId(), $request->request->get('_token'))) {
            $entityManager->remove($utilisateur);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_utilisateur_index', [], Response::HTTP_SEE_OTHER);
    }
    #[Route("/utilisateur/{id}/editRole", name: "app_utilisateur_edit_role", methods: ["GET", "POST"])]
    #[IsGranted("ROLE_ADMIN")]
    public function editRole(Request $request, Utilisateur $utilisateur): Response
    {
        // Check if the user exists
        if (!$utilisateur) {
            throw $this->createNotFoundException('User not found');
        }

        // Retrieve user's roles
        $roles = $utilisateur->getRoles();

        // Check if the user has ROLE_USER and ROLE_ADMIN roles
        //$hasUserRole = in_array('ROLE_USER', $roles);
        $hasAdminRole = in_array('ROLE_ADMIN', $roles);

        // Logic to update user's role based on the current roles
        if ($hasAdminRole) {
            // User has both ROLE_USER and ROLE_ADMIN roles
            // Update user's role to ROLE_USER only
            $utilisateur->setRoles(['ROLE_USER']);
        } else {
            // User has only ROLE_USER role
            // Update user's role to ROLE_ADMIN
            $utilisateur->setRoles(['ROLE_ADMIN']);
        }

        // Persist the changes to the database
        $entityManager = $this->getDoctrine()->getManager();
        $entityManager->flush();

        // Redirect to a success page or return a response
        return $this->redirectToRoute('app_utilisateur_index');
    }
}
