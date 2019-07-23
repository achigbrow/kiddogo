# Kiddo Go


* [User stories](docs/user-stories.md)
* [Wireframes](docs/wire-frames.md)
* [Entity Relationships](docs/erd.md)
* [Milestone Summary](docs/milestone-2.md)

This app is a simple list of tasks that must be completed in a certain time on selected days. The 
tasks will be displayed as individual pages and reward messages will play when the user completes 
all tasks in the required time.


## Users
V1 of this app is geared toward parents of children ages 4-8 who may need assistance in providing a 
structured task/reward system for their children. Future iterations of the app will evolve to appeal 
to teachers/educational assistants for classroom applications, individuals looking toward 
self-improvement through positive habit creation, and managers looking for a metrics-management 
system.


## External Connections
~~I intend to use IBM Watson api to translate text to speech so that the tasks can be verbalized for 
kids who are not yet reading. I will also use Google calendar api for US holidays and (if I am able)
to pull a monthly calendar view onto the screen.~~

### Updated External Connections
The IBM Watson dependencies did not play well with androidx, so I am using the android class built for the purpose of text to speech.
I am changing the connection to Google Calendar. Now, Kiddo Go will push a calendar notification to the parent when the child completes,
or fails to complete, the tasks on time.

## Persistent Data
Persistent data for the app will include the settings selected by the parent to establish timing, 
tasks, and the child's record going back one month. 

Primary navigation will be bottom buttons and tapping/swiping between completed tasks. 
