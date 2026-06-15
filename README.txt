Created by: Timothy Murray, Alston Lin, Anvi Kurade, and Shreyas Aryah


Run and Compile
Compile SocialMediaServer.java and Client.java and run them both.

Enum
- InteractionType: This outlines examples of actions that the user would be able to do in a chatroom, subject to change.

Interfaces
- PageInterface: This interface will serve as a basis to any page classes we'll need in future phases. It contains methods that any page class needs to be sustainable and reasonably modifiable.
All other Interfaces are Interfaces of their respective classes and serve as a basis for those classes.

Classes
- Message: The message class will be the message that the user will send. It contains the message and image variables as well as their respective IDs that will be applied in later phases. The message variable contains the String contents of a Message object while the image variable will contain the file name for an image if the user chooses to send one alongside the message. It also contains 2 String objects each representing a username from a User object from a sender and a receiver.
- User: This class represents the User object and will keep track of all users using the program. This class contains basic information about a User, such as their username, password, and profile picture (stored as a file name String) as well as their user ID and a friend/block list of users they have friended or blocked. The class contains methods to retrieve or modify these variables as necessary.
- UsernameTakenException: this class extends the Exception class and is thrown when the database detects that a username is already in use.
- Chatroom: this is the first class that implements PageInterface and is the only page-type class we felt necessary to be able to run the database. Each chatroom has 2 User objects of the users communicating with each other. Chatroom stores Message objects and adds/modifies/deletes them as necessary.
- Database: This is the brain of the program and is the most crucial part of the program's functionality. This class can manage and search for Users and Chatrooms. When creating a user to add into the database, it is able to throw the UsernameTakenException.
- SocialMediaServer: Connects to the Database so it can relay information to the Client. Accesses ClientHandler to ensure it can safely interact with multiple Client objects.
- ClientHandler: Manages communication between the Server and a Client. Handles tasks like logging in, creating accounts, adding friends, and sending messages. Each Client gets their own ClientHandler that runs in a separate thread, so the server can handle multiple clients simulataneously. The class works with the Database to check user credentials, manage friendships, and enable messaging between users through chatrooms.
- Client: This is how the User interacts with the Server which in turn will relay information from the Database to the Client so the User can access said information.
- EditProfileFrame: Allows the Client's User to edit their User info
- HomeFrame: Default page that the Client will see
- LoginFrame: Allows the Client to access a specific User's data
- MessagingFrame: Allows the Client's User to chat with other Users via the server
- ProfileFrame: Displays User info of the User accessing the Client
- SearchUserFrame: Allows the Client to look up Users in the Database
- UserProfileFrame: Displays User info of other Users to the Client

Testing
Test cases were used to validate the output of each method of each class. The methods and constructors were also given invalid input to check for error handling of each method. This was used to verify that all fields, constructors, and methods were operating correctly.

Submitted on Vocareum by: Anvi Kurade
