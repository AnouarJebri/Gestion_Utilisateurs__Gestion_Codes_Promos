<?php

namespace App\Entity;

use App\Repository\UtilisateurRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Validator\Constraints as Assert;


#[ORM\Entity(repositoryClass: UtilisateurRepository::class)]
#[UniqueEntity(fields: ['email'], message: 'There is already an account with this email')]
class Utilisateur implements UserInterface, PasswordAuthenticatedUserInterface
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;
    #[ORM\Column]
    private ?int $count = 0;

    #[ORM\Column(length: 180, unique: true)]
    #[Assert\NotBlank]
    #[Assert\Email]
    private ?string $email = null;

    #[ORM\Column]
    private array $roles = [];

    /**
     * @var string The hashed password
     */
    #[ORM\Column]
    private ?string $password = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    private ?string $nom = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    private ?string $prenom = null;

    #[ORM\Column]
    #[Assert\NotNull]
    private ?bool $gender = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank]
    private ?string $profession = null;

    #[ORM\Column]
    private ?float $solde = null;

    #[ORM\Column]
    private ?bool $enLigne = null;

    #[ORM\OneToMany(targetEntity: CodePromos::class, mappedBy: 'utilisateur')]
    private Collection $id_cp;

    #[ORM\Column(type: 'boolean')]
    private $isVerified = false;

    #[Assert\NotNull]
    #[ORM\Column(type: Types::DATE_MUTABLE, nullable: true)]
    private ?\DateTimeInterface $birthDay = null;

    public function __construct()
    {
        $this->id_cp = new ArrayCollection();
        $this->enLigne=false;
        $this->solde=0;
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(string $email): static
    {
        $this->email = $email;

        return $this;
    }

    /**
     * A visual identifier that represents this user.
     *
     * @see UserInterface
     */
    public function getUserIdentifier(): string
    {
        return (string) $this->email;
    }

    /**
     * @deprecated since Symfony 5.3, use getUserIdentifier instead
     */
    public function getUsername(): string
    {
        return (string) $this->email;
    }

    /**
     * @see UserInterface
     */
    public function getRoles(): array
    {
        $roles = $this->roles;
        // guarantee every user at least has ROLE_USER
        $roles[] = 'ROLE_USER';

        return array_unique($roles);
    }

    public function setRoles(array $roles): static
    {
        $this->roles = $roles;

        return $this;
    }

    /**
     * @see PasswordAuthenticatedUserInterface
     */
    public function getPassword(): string
    {
        return $this->password;
    }

    public function setPassword(string $password): static
    {
        $this->password = $password;

        return $this;
    }

    /**
     * Returning a salt is only needed, if you are not using a modern
     * hashing algorithm (e.g. bcrypt or sodium) in your security.yaml.
     *
     * @see UserInterface
     */
    public function getSalt(): ?string
    {
        return null;
    }

    /**
     * @see UserInterface
     */
    public function eraseCredentials(): void
    {
        // If you store any temporary, sensitive data on the user, clear it here
        // $this->plainPassword = null;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): static
    {
        $this->nom = $nom;

        return $this;
    }

    public function getPrenom(): ?string
    {
        return $this->prenom;
    }

    public function setPrenom(string $prenom): static
    {
        $this->prenom = $prenom;

        return $this;
    }

    public function isGender(): ?bool
    {
        return $this->gender;
    }

    public function setGender(bool $gender): static
    {
        $this->gender = $gender;

        return $this;
    }

    public function getProfession(): ?string
    {
        return $this->profession;
    }

    public function setProfession(string $profession): static
    {
        $this->profession = $profession;

        return $this;
    }

    public function getSolde(): ?float
    {
        return $this->solde;
    }

    public function setSolde(float $solde): static
    {
        $this->solde = $solde;

        return $this;
    }

    public function isEnLigne(): ?bool
    {
        return $this->enLigne;
    }

    public function setEnLigne(bool $enLigne): static
    {
        $this->enLigne = $enLigne;

        return $this;
    }

    /**
     * @return Collection<int, CodePromos>
     */
    public function getIdCp(): Collection
    {
        return $this->id_cp;
    }

    public function addIdCp(CodePromos $idCp): static
    {
        if (!$this->id_cp->contains($idCp)) {
            $this->id_cp->add($idCp);
            $idCp->setUtilisateur($this);
        }

        return $this;
    }

    public function removeIdCp(CodePromos $idCp): static
    {
        if ($this->id_cp->removeElement($idCp)) {
            // set the owning side to null (unless already changed)
            if ($idCp->getUtilisateur() === $this) {
                $idCp->setUtilisateur(null);
            }
        }

        return $this;
    }

    public function isVerified(): bool
    {
        return $this->isVerified;
    }

    public function setIsVerified(bool $isVerified): static
    {
        $this->isVerified = $isVerified;

        return $this;
    }

    public function getBirthDay(): ?\DateTimeInterface
    {
        return $this->birthDay;
    }

    public function setBirthDay(?\DateTimeInterface $birthDay): static
    {
        $this->birthDay = $birthDay;

        return $this;
    }

}
