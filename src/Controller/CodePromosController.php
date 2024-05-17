<?php

namespace App\Controller;

use App\Entity\CodePromos;
use App\Form\CodePromos1Type;
use App\Repository\CodePromosRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/code/promos')]
class CodePromosController extends AbstractController
{
    #[Route('/', name: 'app_code_promos_index', methods: ['GET'])]
    public function index(CodePromosRepository $codePromosRepository): Response
    {
        return $this->render('code_promos/index.html.twig', [
            'code_promos' => $codePromosRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_code_promos_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $codePromo = new CodePromos();
        $form = $this->createForm(CodePromos1Type::class, $codePromo);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($codePromo);
            $entityManager->flush();

            return $this->redirectToRoute('app_code_promos_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('code_promos/new.html.twig', [
            'code_promo' => $codePromo,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_code_promos_show', methods: ['GET'])]
    public function show(CodePromos $codePromo): Response
    {
        return $this->render('code_promos/show.html.twig', [
            'code_promo' => $codePromo,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_code_promos_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, CodePromos $codePromo, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(CodePromos1Type::class, $codePromo);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_code_promos_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('code_promos/edit.html.twig', [
            'code_promo' => $codePromo,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_code_promos_delete', methods: ['POST'])]
    public function delete(Request $request, CodePromos $codePromo, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$codePromo->getId(), $request->request->get('_token'))) {
            $entityManager->remove($codePromo);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_code_promos_index', [], Response::HTTP_SEE_OTHER);
    }
}
