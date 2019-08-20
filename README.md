# Solar-Decathlon-Summer-Internship
Robust energy analysis of a house

Whilst on the Vanderbilt Solar Decathlon team, I was frustrated with how confusing the energy modeling software e-QUEST is. So for fun, I made my own! It uses integration, change-of-basis formulae, trig, and basic thermodynamics.

This repo contains code I wrote to perform robust energy analysis of a house, calculating instantaneous heating and cooling loads due to the geometry, geographic location, and physical properties of the solar envelope, as well as internal heat gains from appliances and people. 

The root directory contain text files for each room and obstruction that encode their properties (location, dimensions, thermal properties, etc.). The UserInterface.java executable in ~/src/ allows the user to edit/create new rooms and obstructions. The HouseEnergyAnalysis.java executable in ~/src/ runs the model. You can change the hyperparameters (house coordinates, local weather info, etc.) at the top of this file.

Note: I made this after one semester of Java. Looking back, the UI definitely leaves something to be desired :P 
