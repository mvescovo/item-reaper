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
<img src="https://cloud.githubusercontent.com/assets/15829736/25562235/a3f27eca-2dc3-11e7-8eca-fc3f84e88597.png" height="500" width="281"> <img src="https://cloud.githubusercontent.com/assets/15829736/25464120/02160752-2b3e-11e7-8af3-646a09fed5df.png" height="500" width="281"> <img src="https://cloud.githubusercontent.com/assets/15829736/25464119/0204c49c-2b3e-11e7-9890-948f87f6c1b4.png" height="500" width="281">

###### 7" Tablet:

<img src="https://cloud.githubusercontent.com/assets/15829736/25562245/dac3a4e2-2dc3-11e7-8789-e839d89be840.png" height="500" width="800">
<img src="https://cloud.githubusercontent.com/assets/15829736/25562252/f74c5cda-2dc3-11e7-8c15-b28bdde5537b.png" height="500" width="800">
<img src="https://cloud.githubusercontent.com/assets/15829736/25562253/07846ab6-2dc4-11e7-8f01-79620fd2aa90.png" height="500" width="800">

###### 10" tablet:

<img src="https://cloud.githubusercontent.com/assets/15829736/25562256/1598e7bc-2dc4-11e7-9885-2a2b445c11c4.png" height="500" width="800">
<img src="https://cloud.githubusercontent.com/assets/15829736/25562259/21a32004-2dc4-11e7-93f3-8918c8c30407.png" height="500" width="800">
<img src="https://cloud.githubusercontent.com/assets/15829736/25562260/2e37d878-2dc4-11e7-8159-869dd830d714.png" height="500" width="800">

###### Widget:

<img src="https://cloud.githubusercontent.com/assets/15829736/25464167/2a8a8dde-2b3e-11e7-80f2-2a9134ff80e4.png" height="200" width="300">

# Install
- Create a Firebase project with your debug key added to it
- Download the "google-service.json" file and put it in the app directory
- You should then be able to build and install the app to a local device or emulator

# Licence

[GNU General Public License v3.0](http://choosealicense.com/licenses/gpl-3.0/)
