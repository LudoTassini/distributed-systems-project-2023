package project.CLI;

import project.Client;
import project.Model.Notification;
import project.Model.Peer;
import project.Model.Room;
import project.Model.RoomMessage;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class CLI {

    private static final PrintStream out = System.out;

    private static final String BOLD = "\033[1m";
    private static final String YELLOW = "\033[33m";
    private static final String GREEN = "\033[32m";
    private static final String RED = "\033[31m";
    private static final String BLUE = "\033[34m";
    private static final String VIOLET = "\033[35m\033[45m";
    private static final String ORANGE = "\033[33m";
    private static final String RESET = "\033[0m";

    private static final int WIDTH = 100;
    private static final String PADDING = "  ";

    private static final String APP_HEADER = BOLD + "HACO Group Chat v1.0.0" + RESET;
    private static final String ASK_FOR_INPUT = "haco-v1.0.0> ";

    private static final List<Notification> notifications = new ArrayList<>();

    public static void printJoin(){
        //clearConsole();
        drawContainer(APP_HEADER, false);
        drawContainer(BOLD + "Welcome to the highly available, causally ordered group chat!\n" + RESET + PADDING + "Please, enter a nickname:", false);
        out.print("> ");
    }

    public static void printMenu(Client user, boolean showHelp){
        //clearConsole();
        drawContainer(APP_HEADER, false);
        drawContainer("Your nickname: " + BOLD + BLUE + user.getPeerData().getUsername() + RESET + "\n" +
                PADDING + "Your UUID: " + user.getPeerData().getIdentifier() , false);
        drawContainer(BOLD + "Notification center:\n" + RESET + formatNotifications(), !showHelp);
        if(showHelp){
            drawContainer("Available commands:\n" + PADDING +
                    BOLD + "create " + RESET + "[room_name] -> creates a room with the given name\n" + PADDING +
                    BOLD + "chat " + RESET + "[room_name] -> enters the room with the given name\n" + PADDING +
                    BOLD + "delete " + RESET + "[room_name] -> deletes the room with the given name\n" + PADDING +
                    BOLD + "list " + RESET + "[peers|rooms] -> prints the reachable peers or the available rooms, based on the given command\n" + PADDING +
                    BOLD + "update" + RESET + " -> refreshes the interface, potentially showing new notifications\n" + PADDING +
                    BOLD + "discover" + RESET + " -> pings all the nearby peers\n" + PADDING +
                    BOLD + "help" + RESET + " -> prints this explanation\n" + PADDING +
                    BOLD + "quit" + RESET + " -> safely quits the application", true);
        }
    }

    public static void printPeers(Set<Peer> peers){
        int i=1;
        for (Peer peer : peers) {
            out.println();
            out.println("Peer"+i+":");
            out.println("\tID: " + peer.getIdentifier());
            out.println("\tUsername: " + peer.getUsername());
            out.println();
            i++;
        }
    }

    public static void printRooms(Set<Room> createdRooms, Set<Room> participatingRooms){
        int index = 1;
        if (!createdRooms.isEmpty()) {
            out.println("Created Rooms: ");
            for (Room r : createdRooms){
                out.println("Room "+index+" : "+r.getName());
                out.println("ID: "+r.getIdentifier());
                index++;
            }
            out.println();
        }
        if (!participatingRooms.isEmpty()) {
            out.println("Participating Rooms: ");
            for (Room r : participatingRooms){
                out.println("Room "+index+" : "+r.getName());
                out.println("ID: "+r.getIdentifier());
                index++;
            }
            out.println();
        }
        if (participatingRooms.isEmpty() && createdRooms.isEmpty()) {
            out.println("There are no rooms yet!");
        }
    }

    public static void printRoomsInfo(List<Room> roomList){
        out.println("Choose the room you want to delete: ");
        IntStream.range(0, roomList.size())
                .forEach(i -> {
                    out.println("Room " + (i + 1));
                    printRoomInfo(roomList.get(i));
                });
    }

    public static void printRoomInfo(Room room){
        out.println("Room name: " + room.getName());
        out.println("Room ID: " + room.getIdentifier());
        out.println("Room participants: " + room.getRoomMembers());
    }

    public static void printRoomMessages(List<RoomMessage> roomMessages){
        for(RoomMessage roomMessage: roomMessages) {
            if (roomMessage.writtenByMe()) {
                out.println("[" + roomMessage.author().getUsername() + "]: " + BOLD + roomMessage.content() + RESET);
            } else {
                out.println("[" + roomMessage.author().getUsername() + "]: " + roomMessage.content());
            }
        }
    }

    public static void printQuestion(String string){
        out.println(BOLD + string + RESET);
    }

    public static void printDebug(String string){
        out.println(BOLD + ORANGE + string + RESET);
    }

    private static String formatNotifications(){
        if(!notifications.isEmpty()) {
            String result = PADDING;
            for (Notification notification : notifications) {
                switch(notification.type()){
                    case SUCCESS -> result = result.concat(BOLD + GREEN + notification.content() + RESET);
                    case WARNING -> result = result.concat(BOLD + YELLOW + notification.content() + RESET);
                    case ERROR -> result = result.concat(BOLD + RED + notification.content() + RESET);
                    case INFO -> result = result.concat(BOLD + VIOLET + notification.content() + RESET);
                }
            }
            notifications.clear();
            return result;
        }
        else{
            return PADDING + "None.";
        }
    }

    private static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            out.println("Could not clean the console: " + e.getMessage());
        }
    }

    public static void appendNotification(Notification notification){
        notifications.add(notification);
    }

    private static void drawContainer(String content, boolean isFinal) {

        // Disegna il bordo superiore
        out.print("+");
        for (int i = 0; i < WIDTH - 2; i++) {
            out.print("-");
        }
        out.println("+");

        out.println(PADDING + content);

        if(isFinal) {
            // Disegna il bordo inferiore
            out.print("+");
            for (int i = 0; i < WIDTH - 2; i++) {
                out.print("-");
            }
            out.println("+");

            out.print(ASK_FOR_INPUT);
        }
    }

}
