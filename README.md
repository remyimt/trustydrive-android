L'API minimum de l'application est 19 (Android Kitkat = 4.4). Cela est imposé par la dépendance au SDK de Dropbox et est set L8 de build.gradle (module app)

Fonctionnalités et améliorations à apporter :

* Déplacer un fichier et un dossier : Créer une nouvelle activité, qui réutilise les bases de fileListActivity (pour le layout xml), en ajoutant un bouton ok en bas. Dans le fileApdater, rendre les fichiers non clickable et grisâtre. Lors de la navigation, enregistré le path comme dans fileListActivity, et le comparer au path initial lorsque que l'utilisateur click sur ok pour éviter les cas tel que la copie au même niveau ou dans un sous dossier.

* Crypter les metadatas : Cryptage avant la L39 de UpdateTask et décryptage avant la L71 de LoginTask.

* Delete récursif : L150 de fileListActivity. Se baser sur checkOnDevice L82.

* Chunks de 500kb : Modifier les tasks. Attention, lors de l'ajout d'un fichier (onAcitivityResult L102 de fileLIstAcitivty), size peut parfois être nul, et la méthode available retourne une "estimation" d'après la javaDoc. La création du nombre exact de chunks devra peut-être être transféré dans l'UploadTask. 

* Appels asynchrones : Modifier les tasks. L'utilisation des future java est la solution que je testerais. A savoir : les méthodes onPostExecute s'exécute sur l'UI thread, donc l'utilisation d'un await dans une activité ou une méthode onPostExecute empêche toutes les autres taches d'exécuter leur onPostExecute. 

* Déconnecter l'utilisateur après inactivité : La solution à laquelle j'ai pensé est d'ajouter une variable dans le singleton (DataHolder), qui est un timestamp. A chaque onResume, calculer le temps écoulé, si trop long logout, sinon actualisé le timestamp

* Flèche retour dans la navigation : Le seul moyen actuel pour naviguer dans les dossiers est d'utiliser la flèche retour en bas du smartphone. Sur certain Android, celle-ci n'est pas facile d'accès, un peu serait donc d'ajouter un icone dans l'appBar. Il faudra pour cela utiliser les méthodes onCreateOptionsMenu et onOptionsItemSelected (L133 et L138 de fileListAcitivty)

* Sélecteur d’applications : FileListActivity L255 : Modifier le type lors de la création de l'intent en fonction du fichier. Peut-être que librairies existes pour détecter le type du fichier et éviter de faire le parcage à la main.