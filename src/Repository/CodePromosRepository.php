<?php

namespace App\Repository;

use App\Entity\CodePromos;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<CodePromos>
 *
 * @method CodePromos|null find($id, $lockMode = null, $lockVersion = null)
 * @method CodePromos|null findOneBy(array $criteria, array $orderBy = null)
 * @method CodePromos[]    findAll()
 * @method CodePromos[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class CodePromosRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, CodePromos::class);
    }

//    /**
//     * @return CodePromos[] Returns an array of CodePromos objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('c')
//            ->andWhere('c.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('c.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?CodePromos
//    {
//        return $this->createQueryBuilder('c')
//            ->andWhere('c.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
