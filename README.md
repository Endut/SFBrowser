# SFBrowser
SFBrowser searches in a database of soundfiles by name using the utility 'locate'
first edit the $SEARCHPATHS variable in sflocate.updatedb to a list of where you keep your sound files 
(eg could just be $HOME)
you make the database by running sflocate.updatedb (don't forget to chmod 755 sflocate.updatedb first)
after you do that SFBrowser should be ready
just run SFBrowser.show;
this returns an EZListView so you can edit its behaviour
in the list view spacebar plays the file using 'mplayer' and enter loads it into an 'SPP' object 
drag an item and drop its path onto things - including scide or sublime text :)


/Users/adamjuraszek/Library/Application Support/SuperCollider/Extensions/AJLIB/myclasses/SFBrowser/Screen Shot 2016-07-20 at 14.16.57.png



![Alt text](./Screen\ Shot\ 2016-07-20\ at\ 14.16.57.png?raw=true "all my claps")