#Implementation Summary

The current implementation of Kiddo Go contains:
* an Activities Database, User entity, and Activity entity.
* daos for User and Actity.
* a View Model for User.

The opening page opens to a button whose text is set by the pre-populated data in the User entity. This is the only place to directly
observe the data in the database at this point. Clicking on the button with the name opens a slider activity that runs the user through a list of tasks. Each slide has a button (DONE!) that currently only returns a boolean value
when clicked.

The settings icon opens a preference settings where the user can enter the name of one chile, enter their own email, and set how long the child
has to complete the tasks. Entering a name populates a new user in the User entity.

The play button does nothing when you click it. The Dasboard button crashes the app.
