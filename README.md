# Android - Mobile Services - Tic Tac Toe Leaderboard
This is an Android leaderboard sample which makes use of Windows Azure Mobile Services for it's data storage.  It will track different player's wins, losses, and ties from playing a game of Tic Tac Toe.  This sample was built using Eclipse, the Android SDK, and the Andorid Mobile Services SDK.  It was built using a minimum SDK version of 8 and a target version of 17.

Below you will find requirements and deployment instructions.

## Requirements
* Eclipse - This sample was built with Eclipse Indigo.
* Android SDK - You can download this from the [Android Developer portal](http://developer.android.com/sdk/index.html).
* Windows Azure Account - Needed to create and run the Mobile Service.  [Sign up for a free trial](https://www.windowsazure.com/en-us/pricing/free-trial/).

## Source Code Folders
* /source/start - This contains code for the application prior to adding in Mobile Services.
* /source/end - This contains code for the application with Mobile Services and requires client side changes noted below.
* /source/scripts - This contains server side scripts you'll need to set on your Mobile Service as noted below.

## Additional Resources


#Setting up your Mobile Service
After creating your Mobile Service in the Windows Azure Portal, you'll need to create a table named "PlayerRecords".  After creating that table, alter it's insert and read scripts using the following scripts:
* /source/scripts/PlayerRecords.insert.script
* /source/scripts/PlayerRecords.read.script

#Client Application Changes
In order to run the client applicaiton, you'll need to change a few settings in your application.  After importing the project into Eclipse, open TicTacToeService.java file.  In the constructor method, change the \<YourMobileServiceUrl> and \<YourApplicationKey> to match the values from the Mobile Service you've created.

## Contact

For additional questions or feedback, please contact the [team](mailto:chrisner@microsoft.com).
