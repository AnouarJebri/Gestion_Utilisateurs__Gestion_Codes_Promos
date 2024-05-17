<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class CoursesAndServicesController extends AbstractController
{
    #[Route('/courses/and/services', name: 'app_courses_and_services')]
    public function index(): Response
    {
        return $this->render('courses_and_services/index.html.twig', [
            'controller_name' => 'CoursesAndServicesController',
        ]);
    }
}
