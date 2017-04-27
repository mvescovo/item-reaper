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

# Install
- Create a Firebase project with your debug key added to it
- Download the "google-service.json" file and put it in the app directory
- You should then be able to build and install the app to a local device or emulator

# Licence

[GNU General Public License v3.0](http://choosealicense.com/licenses/gpl-3.0/)