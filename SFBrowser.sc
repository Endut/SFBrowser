SFBrowser {
	classvar <soundFiles;
	classvar <>updateScript;
	classvar <>database;
	classvar <window, searchField, listView;
	classvar <>editor, <>player;
	*initClass {
		//contentLocations = ["/Users/adamjuraszek/Sounds", "/Volumes/DATA/extrasounds", "/Volumes/DATA/Recordings"];
		var thisFolder = this.class.filenameSymbol.asString.dirname;
		//thisFolder.asString.dirname.postln;
		player = "/usr/local/bin/mplayer -quiet ";
		updateScript = thisFolder +/+ "sflocate.updatedb";
		database = "~/Library/sflocate.database";
		soundFiles = ("locate -d " ++ database ++ " *").unixCmdGetStdOutLines;
		soundFiles = soundFiles.collect({ arg path; Association.new(path.basename.splitext.at(0), path)});
		"soundfile list populated".postln;
		
	}
	
	
	

	*show {
		var find, audition, load, playerPid, killPlayer;
		thisProcess.postln;
		//make window
		window = Window.new(bounds: Rect(132, 359, 150, 396)).front;
		window.layout = VLayout();
		window.layout.spacing = 0;
		window.layout.margins = 0!4;


		//make search field
		searchField = TextField(window);
		searchField.background_(Color.clear);
		searchField.string = "";

		// create list view
		listView = EZListView(window, bounds: window.bounds)
			.globalAction_({ arg obj; var thing = obj.items.at(obj.value).value;
				thing.postln;
				thing;
			 });

		listView.listView.background = Color.clear;
		listView.items_(soundFiles);


		// search func
		find = { arg query;
			if ( query == "", {
				listView.items_(soundFiles);
				},
				{
					var filtered = ("locate -d ~/Library/sflocate.database -i "++query).unixCmdGetStdOutLines;
					filtered = filtered.collect({ arg path; Association.new(path.basename.splitext.at(0), path) });
					listView.items_(filtered);

					});
		};
		killPlayer = {
			("kill "++playerPid).unixCmd;
		};
		CmdPeriod.add(killPlayer);

		// functions for auditioning and loading and editing
		audition = { arg path;
			killPlayer.();
			//("/usr/local/bin/ffprobe -hide_banner "++path.shellQuote).unixCmd;
			playerPid = (player ++ path.shellQuote).unixCmd;
			listView.listView.focus( true )
		};

		load = { arg path; ("loading " ++ path).postln; SPP.new(path) };


		listView.listView.keyDownAction = { arg view, char, mod, unicode, keycode, key;
			//[view, char, mod, unicode, keycode, key].postln;
			if (unicode == 32, { audition.(listView.items.at(listView.doAction.value).value) });
			if (unicode == 13, { load.(listView.items.at(listView.doAction.value).value) });
		};

		// search box actions
		searchField.action = { arg field; var search = field.value; find.(search)  };

		searchField.keyUpAction = { arg view; view.doAction; };

		searchField.asView.keyDownAction = { arg view, char, mod, unicode, keycode, key;
			if ( keycode == 125, { listView.listView.focus(true )});
		};


		listView.listView.beginDragAction = { arg view;
			listView.items.at(listView.doAction.value).value;
		};




		^listView

	}
	*rescan {
		updateScript.shellQuote.unixCmd;
		window.close;
		super.initClass;
		super.show;
	}
}