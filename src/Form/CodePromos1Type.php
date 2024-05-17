<?php

namespace App\Form;

use App\Entity\CodePromos;
use App\Entity\Utilisateur;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class CodePromos1Type extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('dateExpiration')
            ->add('code')
            ->add('utilisateur', EntityType::class, [
                'class' => Utilisateur::class,
                'choice_label' => 'username', // Adjust this to the property you want to display in the dropdown
                'placeholder' => 'Select a user', // Optional placeholder text
                'required' => false, // Change to true if the field is mandatory
            ]);
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => CodePromos::class,
        ]);
    }
}
