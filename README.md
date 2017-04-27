[![Build Status](https://travis-ci.org/mvescovo/item-reaper.svg?branch=master)](https://travis-ci.org/mvescovo/item-reaper)
[![AUR](https://img.shields.io/aur/license/yaourt.svg)](https://choosealicense.com/licenses/gpl-3.0/)

# Item Reaper
Have things in your house you no longer need or use? Want to be more efficient with your
shopping? Item Reaper is designed just for this purpose. It’s job is to let you know when an
item’s time is up. It’s the Grim Reaper for items. Know what you need and when. Prioritise
based on your actual needs using your own data.

This app is for anyone who might find it helpful to catalogue their own items. If you went
shopping right now, what items would you need the most? How much did you pay last time?
These are the types of questions Item Reaper intends to answer.

# Features
- Add/edit items
  - Save item data to Firebase real time database
- Take a picture of an item or select an item with the picker
  - Save and retrieve pictures using Firebase storage
- View list of items
  - Sort by expiry or purchase date
- Search for an item using any of the item details
- View item details
- Expire an item (hides from list), or delete an item
  - Undo button on snackbar
- About page including attributions for Reaper artwork (icons) and sound effect
- Includes a widget to view the list of items
  - Can click an item to view details
  - Can click to create a new item
- Displays Admob ad in detail view
- Uses Firebase Analytics to try to understand user behavior within the app

# Screenshots

###### Phone:
<img src="https://cloud.githubusercontent.com/assets/15829736/25464118/02028984-2b3e-11e7-93cd-4aee851f1117.png" height="500" width="281"> <img src="https://cloud.githubusercontent.com/assets/15829736/25464120/02160752-2b3e-11e7-8af3-646a09fed5df.png" height="500" width="281"> <img src="https://cloud.githubusercontent.com/assets/15829736/25464119/0204c49c-2b3e-11e7-9890-948f87f6c1b4.png" height="500" width="281">

###### Tablet:

<img src="https://cloud.githubusercontent.com/assets/15829736/25464136/16b29f9a-2b3e-11e7-9fec-6577cde9e994.png" height="500" width="667">
<img src="https://cloud.githubusercontent.com/assets/15829736/25464137/16b7ed4c-2b3e-11e7-9002-728c9c9926ff.png" height="500" width="667">
<img src="https://cloud.githubusercontent.com/assets/15829736/25464138/16b87abe-2b3e-11e7-9efe-ce11aa7bea10.png" height="500" width="667">

###### 10" tablet:

<img src="https://cloud.githubusercontent.com/assets/15829736/25464147/1fdbd6ae-2b3e-11e7-95cd-1636ef62d9c4.png" height="500" width="800">
<img src="https://cloud.githubusercontent.com/assets/15829736/25464148/200b94ca-2b3e-11e7-938e-1be6f941dc0d.png" height="500" width="800">
<img src="https://cloud.githubusercontent.com/assets/15829736/25464149/2046badc-2b3e-11e7-88c2-65db35a982ba.png" height="500" width="800">

###### Widget:

<img src="https://cloud.githubusercontent.com/assets/15829736/25464167/2a8a8dde-2b3e-11e7-80f2-2a9134ff80e4.png" height="200" width="300">

# Install
- Create a Firebase project with your debug key added to it
- Download the "google-service.json" file and put it in the app directory
- You should then be able to build and install the app to a local device or emulator

# Licence

[GNU General Public License v3.0](http://choosealicense.com/licenses/gpl-3.0/)
