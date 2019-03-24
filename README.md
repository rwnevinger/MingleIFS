MingleION Project

UserXml class will create a Mingle loaduser.xml to create/update Mingle IDs in bulk.
General workflow 
reads a userxml.properties files
REST call to Mingle/SocialService.svs/User/%20/AllUsers to read all Mingle UserIDs.  The UserName property is parsed from json stored to 
a MingleUserName hashmap key=mingleid value=username.  The username will be used to validate the IdentityActor ssopv2 service identity
REST call to Actor/lists/_generic?_fields=Actor&_filter=IsMingleUser::tru_&
ActorCount is set, and two more hashmaps initliazed LandmarkActors mingleid keyed by actor,  MingleIDActor actor keyed by mingleid
SecurityTemplate is then parsed, and an Actor is either updated or created for each record in the template
Finally loaduser.xml file is generated as of right now in /mnt/oneoncology/TST/IFS



execute jar
java -jar bin/mingleion.jar SCMUserRoles.csv
java -jar bin/mingleion.jar oneonc_cs_security.csv


git commands

git init
git add pom.xml
git add src
vi .gitignore
git add .gitignore
git add bin

mark changes: git commit -a
update repo:  git push https://github.com/rwnevinger/MingleIFS master
force update repo:  git push -f https://github.com/rwnevinger/MingleIFS master

or should this be
update repo:  git push https://github.com/rwnevinger/MingleIFS.git master

updating single file
git commit -m "Create unique actor ids for duplicate actors"
git config user.name "rwnevinger@gmail.com"
git config user.email "rwnevinger@gmail.com"
cd src/main/java/com/oneoncology
git commit -m "Create unique actor ids for duplicate actors"
git push https://github.com/rwnevinger/MingleIFS
Username for 'https://github.com': rwnevinger@gmail.com
Password for 'https://rwnevinger@gmail.com@github.com':
Counting objects: 8, done.
Compressing objects: 100% (6/6), done.
Writing objects: 100% (8/8), 805 bytes | 805.00 KiB/s, done.
Total 8 (delta 3), reused 0 (delta 0)
remote: Resolving deltas: 100% (3/3), completed with 3 local objects.
remote:
remote: GitHub found 8 vulnerabilities on rwnevinger/MingleIFS's default branch (8 high). To find out more, visit:
remote:      https://github.com/rwnevinger/MingleIFS/network/alerts
remote:
To https://github.com/rwnevinger/MingleIFS
   8a19297..c24fd85  master -> master

 pull https://github.com/rwnevinger/MingleIFS
From https://github.com/rwnevinger/MingleIFS
 * branch            HEAD       -> FETCH_HEAD
Updating 8a19297..c24fd85
Fast-forward
 src/main/java/com/oneoncology/UserXml.java | 30 +++++++++++++++++++-----------
 1 file changed, 19 insertions(+), 11 deletions(-)





