## Zoo App — Application mobile Android du parc animalier de la Barben

### Test de l'application

Pour tester l'application :

- Utilisateur classique  
  Email : `user@test.com`  
  Mot de passe : `123456`

- Administrateur  
  Email : `admin@test.com`  
  Mot de passe : `123456`

---

### Fonctionnalités

#### Authentification
- Connexion et inscription via Firebase
- Connexion persistante

#### Accueil
- Affichage de la liste des biomes

#### Enclos
- Liste des enclos d’un biome
- Affichage du statut maintenance
- Affichage de l’heure de nourrissage
- Admin : bouton pour ouvrir/fermer un enclos
- Admin : modification de l’heure de nourrissage avec sélection de l’heure et enregistrement dans Firebase

#### Animaux
- Liste des animaux d’un enclos

#### Avis
- Affichage des avis par enclos
- Moyenne des notes
- Ajout d’un avis (note et commentaire) pour les utilisateurs connectés

#### Services
- Liste des services présents dans le parc (ex : toilettes, café, boutique…)

#### Profil
- Affichage du nom d’utilisateur
- Admin : modification du nom