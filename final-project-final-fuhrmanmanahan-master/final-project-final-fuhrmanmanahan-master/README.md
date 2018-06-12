# final-project-final-fuhrmanmanahan

Melee Stats

Benjamin Fuhrman (bdf7kt) Mark Manahan (mmm5ja)

Current progress:
==================
_________________________________________________
Data Storage
_________________________________________________
- Created a SQL database for the game matches with the following columns: ID, YOURCHAR, OPPNAME, OPPCHAR, XLOC, YLOC, STAGE, OUTCOME
- Created the MatchItem and its Adapter class to allow for use with a Recyler View in order to dynamically show previous entries
- Created shared preferences variables to store information put into the settings page
_________________________________________________
Features
_________________________________________________
- Created the counterpick function which takes in an Opponent's name and character and recommends combinations of stages and matchups
- Created the search results page which displays entries in an organized manner with highlights on specific aspects
_________________________________________________
Layout
_________________________________________________
- Created *most* of the activities that will eventually be made functional each with their own layout
- Added linking between activites and backwards functionality
- Made the layout screens visually appealing, with more work to be done in the future
_________________________________________________
How To Use
==================
_________________________________________________
- Add a new match entry:
  1. Click F.A.B to go to add game screen
  2. Fill in appropriate match information
  3. Select one of the six stages, the outcome of the match, and possibly the multiple checkbox, if playing more than one match (when done adding multiple matches/adding last match, uncheck the multiple checkbox)
  4. Click add
- View recorded matches:
  1. Click Review your matches on loading screen
  2. Keep navigating with the single buttons ("next" in first review activity, leave search-by criteria blank in this activity; "search" in second review activity), until all information is displayed. Note these in between pages will have filters soon.
- Counterpick your opponent:
  1. Click Counterpick your opponent on loading screen
  2. Enter opponent information
  3. Displayed is the recommended next match based of win rates with your character and stage selection
- Change Settings:
  1. Click on the three vertical dots on the top right of the screen
  2. Tap Settings
  3. Change any fields you desire
