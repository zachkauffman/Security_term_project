import java.util.Scanner;
import java.util.List;

public class MyClient {
	
	public MyClient()
	{
	}
	public void serverConnect()
	{
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter the file server address you'd like to connect to: ");
		String fileserver = scan.next();
		
		System.out.print("Enter the file server port you'd like to connect to: ");
		int fileport = scan.nextInt();
		
		System.out.print("Enter the group server address you'd like to connect to: ");
		String groupserver = scan.next();
		
		System.out.print("Enter the group server port you'd like to connect to: ");
		int groupport = scan.nextInt();
		
		//create the client and connect
		FileClient fclient = new FileClient();
		GroupClient gclient = new GroupClient();
		fclient.connect(fileserver, fileport);
		gclient.connect(groupserver, groupport);
		
		System.out.print("Please enter your username: ");
		String username = scan.next();
		UserToken token = gclient.getToken(username);
		
		System.out.print("Enter 1 for file server options, 2 for group server: ");
		int file_or_group = scan.nextInt();
		
		while(file_or_group == 1 || file_or_group == 2)
		{
			if(file_or_group == 1)
			{
				fileMenu(fclient, gclient, username, token);
			}
			else
			{
				groupMenu(fclient, gclient, username, token);
			}
			System.out.print("Enter 1 for file server options, 2 for group server: ");
			file_or_group = scan.nextInt();
		}
	}
	public void fileMenu(FileClient fclient, GroupClient gclient, String username, UserToken token)
	{
		Scanner scan = new Scanner(System.in);
		int choice = -1;
		while(choice!=0)
		{
			System.out.println("File Client Menu :: Options");
			System.out.println("1) List Files");
			System.out.println("2) Upload File");
			System.out.println("3) Download File");
			System.out.println("4) Delete File");
			System.out.println("5) Disconnect");
			System.out.print("Please make a selection (0 to quit): ");
			choice = scan.nextInt();
			
			if(choice == 1)
			{
				fclient.listFiles(token);
			}
			else if(choice == 2)
			{
				System.out.print("Enter the name of the file to upload: ");
				String source = scan.next();
				
				System.out.print("Enter the filename as it will appear on the server: ");
				String destination = scan.next();
				
				System.out.print("Enter the group you'd like to upload to: ");
				String group = scan.next();
				
				fclient.upload(source, destination, group, token);
			}
			else if(choice == 3)
			{
				System.out.print("Enter the name of the file to download: ");
				String source = scan.next();
				
				System.out.print("Enter the filename as it will appear on your computer: ");
				String destination = scan.next();
				
				fclient.download(source, destination, token);
			}
			else if(choice == 4)
			{
				System.out.print("Enter the name of the file to delete: ");
				String source = scan.nextLine();
				
				fclient.delete(source, token);
			}
			else if(choice == 5)
			{
				gclient.disconnect();
				fclient.disconnect();
			}
			else
			{
				System.out.println("Invalid option!");
			}
		}
	}

	public void groupMenu(FileClient fclient, GroupClient gclient, String username, UserToken token)
	{
		Scanner scan = new Scanner(System.in);
		int choice = -1;

		
		while(choice!=0)
		{
			System.out.println("Group Client Menu :: Options");
			System.out.println("1) Create User (Admin only)");
			System.out.println("2) Delete User (Admin only)");
			System.out.println("3) Create Group");
			System.out.println("4) Delete Group");
			System.out.println("5) Add user to group");
			System.out.println("6) Delete user from group");
			System.out.println("7) List users in group");
			System.out.println("8) Disconnect");
			System.out.print("Please make a selection (0 to quit): ");
			choice = scan.nextInt();
			
			if(choice == 1)
			{
				System.out.print("Enter username you would like to create: ");
				String newUser = scan.next();
				if(!gclient.createUser(newUser, token))
				{
					System.out.println("Error! Either you don't have permission, or username already exists");
				}
				else
				{
					System.out.println("Successfully created "+newUser);
				}
			}
			else if(choice == 2)
			{
				System.out.print("Enter username you would like to delete: ");
				String deleteUser = scan.next();
				if(!gclient.deleteUser(deleteUser, token))
				{
					System.out.println("Error! Either you don't have permission, or username doesn't exist");
				}
				else
				{
					System.out.println("Successfully deleted "+deleteUser);
				}
			}
			else if(choice == 3)
			{
				System.out.print("Enter group you would like to create: ");
				String newGroup = scan.next();
				if(!gclient.createGroup(newGroup, token))
				{
					System.out.println("Error! Either you don't have permission, or group already exists");
				}
				else
				{
					System.out.println("Successfully created "+newGroup);
				}
			}
			else if(choice == 4)
			{
				System.out.print("Enter group you would like to delete: ");
				String deleteGroup = scan.next();
				if(!gclient.deleteGroup(deleteGroup, token))
				{
					System.out.println("Error! Either you don't have permission, or group doesn't exists");
				}
				else
				{
					System.out.println("Successfully deleted "+deleteGroup);
				}
			}
			else if(choice == 5)
			{
				System.out.print("Enter group you would like to add to: ");
				String group = scan.next();
				System.out.print("Enter username you would like to add to "+group+": ");
				String user = scan.next();
				
				if(!gclient.addUserToGroup(user, group, token))
				{
					System.out.println("Error! Either you don't have permission, username doesn't exist, or group doesn't exist");
				}
				else
				{
					System.out.println("Successfully added "+username+" to "+group);
				}
			}
			else if(choice == 6)
			{
				System.out.print("Enter group you would like to delete from: ");
				String group = scan.next();
				System.out.print("Enter username you would like to remove from "+group+": ");
				String user = scan.next();
				
				if(!gclient.deleteUserFromGroup(user, group, token))
				{
					System.out.println("Error! Either you don't have permission, username doesn't exist, or group doesn't exist");
				}
				else
				{
					System.out.println("Successfully removed "+username+ " from "+group);
				}
			}
			else if(choice == 7)
			{
				System.out.print("Enter group you would like to list the members of: ");
				String group = scan.next();
				
				List members = gclient.listMembers(group, token);
				
				if(members == null)
				{
					System.out.println("Error! Either you don't have permission, or group doesn't exist");
				}
				else if(members.size() == 0)
				{
					System.out.println("No members");
				}
				else
				{
					for(int i = 0; i<members.size(); i++)
					{
						System.out.println((String)members.get(i));
					}
				}
			}
			else if(choice == 8)
			{
				gclient.disconnect();
				fclient.disconnect();
			}
			else
			{
				System.out.println("Invalid option!");
			}
		}
	}
	
public static void main(String args[]) {

	//holds user's choice for connecting to group or file server
	int server_choice;
	Scanner scan = new Scanner(System.in);
	MyClient c = new MyClient();
	c.serverConnect();
	}
}		