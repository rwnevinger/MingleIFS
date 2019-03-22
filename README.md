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



