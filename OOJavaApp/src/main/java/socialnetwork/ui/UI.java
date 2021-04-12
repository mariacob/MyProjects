//package socialnetwork.ui;
//
//import socialnetwork.domain.Mesaj;
//import socialnetwork.domain.Tuple;
//import socialnetwork.service.ServiceCereriPrietenie;
//import socialnetwork.service.ServiceMesaje;
//import socialnetwork.service.ServicePrietenii;
//import socialnetwork.service.ServiceUtilizatori;
//import socialnetwork.service.validators.RepoException;
//import socialnetwork.service.validators.ValidationException;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.time.LocalDate;
//import java.time.Month;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class UI {
//    private final ServiceUtilizatori servU;
//    private final ServicePrietenii servP;
//    private final ServiceMesaje servM;
//    private final ServiceCereriPrietenie servC;
//    private final BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
//
//    /**
//     * constructor
//     * @param servU
//     * @param servP
//     */
//    public UI(ServiceUtilizatori servU, ServicePrietenii servP, ServiceMesaje servM, ServiceCereriPrietenie servC) {
//
//        this.servP = servP;
//        this.servU = servU;
//        this.servM = servM;
//        this.servC = servC;
//    }
//    private void printMenu(){
//        System.out.println("1. Add user\n2. Remove user\n3. Update user\n4. Print all users\n" +
//                "5. Add friendship\n6. Delete friendship\n7. Print one's friends\n8. Print number of communities\n" +
//                "9. Print largest community\n10. Exit\n11. Print friends of user and date of friendship\n12. Print friends added in a specific month\n" +
//                "13. Send message\n14. Send reply\n15. Print messages in chronological order\n16. Delete message\n" +
//                "17. Send request\n18. Accept request\n19. Refuse request\n20. Delete request");
//        System.out.println("Enter option: ");
//    }
//    private void addUser() throws IOException {
//        try {
//            System.out.println("Enter first name: ");
//            String firstName = reader.readLine();
//            System.out.println("Enter last name: ");
//            String lastName = reader.readLine();
//            servU.addUtilizator(firstName, lastName);
//        }
//        catch (NumberFormatException e) {
//            System.out.println("ID must be number!");
//        }
//        catch (RepoException | IllegalArgumentException | ValidationException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void removeUser() throws IOException {
//        try{
//            System.out.println("Enter ID: ");
//            Long id = Long.parseLong(reader.readLine());
//            servP.deteleFriendshipsOfUser(id);
//            servC.deleteRequestsOfUser(id);
//            servM.removeMessagesOfUser(id);
//            servU.removeEntity(id);
//        }
//        catch (NumberFormatException e) {
//            System.out.println("ID must be number!");
//        }
//        catch (RepoException | IllegalArgumentException | ValidationException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void updateUser() {
//        try {
//            System.out.println("Enter ID: ");
//            Long id = Long.parseLong(reader.readLine());
//            System.out.println("Enter first name: ");
//            String firstName = reader.readLine();
//            System.out.println("Enter last name: ");
//            String lastName = reader.readLine();
//
//            servU.updateUser(id, firstName, lastName);
//        } catch (NumberFormatException | IOException e) {
//            e.printStackTrace();
//        } catch (RepoException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void printAll(){
//        servU.getAll().forEach(System.out::println);
//    }
//
//    private void addFriendship() throws IOException {
//        try {
//            System.out.println("Enter the first id: ");
//            Long id1 = Long.parseLong(reader.readLine());
//            System.out.println("Enter the second id: ");
//            Long id2 = Long.parseLong(reader.readLine());
//            servP.addPrietenie(id1, id2);
//        }     catch (NumberFormatException e) {
//            System.out.println("ID must be number!");
//        }
//        catch (RepoException | IllegalArgumentException | ValidationException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void removeFriendship() throws IOException {
//        try {
//            System.out.println("Enter the first id: ");
//            Long id1 = Long.parseLong(reader.readLine());
//            System.out.println("Enter the second id: ");
//            Long id2 = Long.parseLong(reader.readLine());
//            servP.removeEntity(new Tuple<>(id1, id2));
//        }
//        catch (NumberFormatException e) {
//            System.out.println("ID must be number!");
//        }
//        catch (RepoException | IllegalArgumentException | ValidationException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
////    private void printFriends() throws IOException {
////        try {
////            System.out.println("Enter the id: ");
////            Long id1 = Long.parseLong(reader.readLine());
////            List<Long> l = servU.getFriends(id1);
////            System.out.println("Friends of " + servU.findEntity(id1).getFirstName());
////            l.forEach(x -> System.out.println(servU.findEntity(x)));
////        }
////        catch (NumberFormatException e) {
////            System.out.println("ID must be number!");
////        }
////        catch (RepoException | IllegalArgumentException | ValidationException e){
////            System.out.println(e.getMessage());
////        }
////    }
//
////    void printCommunities(){
////        System.out.println(servP.connectedComponents());
////    }
////
////    void printLargestCom(){
////        servP.largestConnectedComp().forEach(System.out::println);
////    }
//
//    private void printFriendsAndDate() throws IOException {
//        try {
//            System.out.println("Enter the id: ");
//            Long id1 = Long.parseLong(reader.readLine());
//            List<Tuple<Long, LocalDate>> l = servU.getFriendsAndDate(id1);
//            System.out.println("Friends of " + servU.findEntity(id1).getFirstName());
//            l.forEach(x -> System.out.println(servU.findEntity(x.getLeft()) + " " + x.getRight()));
//        }
//        catch (NumberFormatException e) {
//            System.out.println("ID must be number!");
//        }
//        catch (RepoException | IllegalArgumentException | ValidationException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void printFriendsByMonth() throws IOException {
//        try {
//            System.out.println("Enter the id: ");
//            Long id1 = Long.parseLong(reader.readLine());
//            System.out.println("Enter the month: ");
//            Month m = Month.of(Integer.parseInt(reader.readLine()));
//            List<Tuple<Long, LocalDate>> l = servU.getFriendsByMonth(id1,m);
//            System.out.println("Friends of " + servU.findEntity(id1).getFirstName());
//            l.forEach(x -> System.out.println(servU.findEntity(x.getLeft()) + " " + x.getRight()));
//        }
//        catch (NumberFormatException e) {
//            System.out.println("ID must be number!");
//        }
//        catch (RepoException | IllegalArgumentException | ValidationException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void sendMessage() throws IOException {
//        try {
//            System.out.println("To: ");
//            List<Long> to = new ArrayList<>();
//            Arrays.stream(reader.readLine().split(" ")).forEach(id -> to.add(Long.parseLong(id)));
//            System.out.println("From: ");
//            Long from = Long.parseLong(reader.readLine());
//            System.out.println("Message: ");
//            String text = reader.readLine();
//            servM.addMesaj(from, to, text);
//        }
//        catch(RepoException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void sendReply() throws IOException {
//        try {
//            System.out.println("Reply to (message id): ");
//            Long reply = Long.parseLong(reader.readLine());
//            System.out.println("Send to: ");
//            List<Long> to = new ArrayList<>();
//            Arrays.stream(reader.readLine().split(" ")).forEach(id -> to.add(Long.parseLong(id)));
//            System.out.println("From: ");
//            Long from = Long.parseLong(reader.readLine());
//            System.out.println("Message: ");
//            String text = reader.readLine();
//            servM.addReplyMesaj(from, to, text, reply);
//        }
//        catch (RepoException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void printSortedMessages() throws IOException {
//            System.out.println("Enter the first user: ");
//            Long u1 = Long.parseLong(reader.readLine());
//            System.out.println("Enter the second user: ");
//            Long u2 = Long.parseLong(reader.readLine());
//            List<Mesaj> lst = servM.getSortedMesaje(u1,u2);
//            lst.forEach(System.out::println);
//    }
//
//    private void removeMessage() throws IOException {
//        try{
//            System.out.println("Enter the id: ");
//            Long id = Long.parseLong(reader.readLine());
//            servM.removeEntity(id);
//        }
//        catch (RepoException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//    private void sendRequest() throws IOException {
//        try{
//            System.out.println("From user: ");
//            Long id1 = Long.parseLong(reader.readLine());
//            System.out.println("To user: ");
//            Long id2 = Long.parseLong(reader.readLine());
//            servC.addCerere(id1,id2);
//        }catch (RepoException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
////    private void acceptRequest() throws IOException {
////        try {
////            System.out.println("From user: ");
////            Long id1 = Long.parseLong(reader.readLine());
////            System.out.println("To user: ");
////            Long id2 = Long.parseLong(reader.readLine());
////            servC.updateCerere(id1,id2,"APPROVED");
////            servP.addPrietenie(id1,id2);
////        }catch (RepoException e){
////            System.out.println(e.getMessage());
////        }
////    }
////
////    private void refuseRequest() throws IOException {
////        try {
////            System.out.println("From user: ");
////            Long id1 = Long.parseLong(reader.readLine());
////            System.out.println("To user: ");
////            Long id2 = Long.parseLong(reader.readLine());
////            servC.updateCerere(id1,id2,"REJECTED");
////        }catch (RepoException e){
////            System.out.println(e.getMessage());
////        }
////    }
//
//    private void removeRequest() throws IOException {
//        try{
//            System.out.println("From user: ");
//            Long id1 = Long.parseLong(reader.readLine());
//            System.out.println("To user: ");
//            Long id2 = Long.parseLong(reader.readLine());
//            servC.removeEntity(new Tuple<>(id1,id2));
//        }catch (RepoException e){
//            System.out.println(e.getMessage());
//        }
//    }
//
//
//
//    /**
//     * starts the UI
//     * @throws IOException
//     */
//    public void startUI() throws IOException {
//        while(true) {
//            printMenu();
//            String option = reader.readLine();
//            switch (option) {
//                case "1":
//                    addUser();
//                    break;
//                case "2":
//                    removeUser();
//                    break;
//                case "3":
//                    updateUser();
//                    break;
//                case "4":
//                    printAll();
//                    break;
//                case "5":
//                    addFriendship();
//                    break;
//                case "6":
//                    removeFriendship();
//                    break;
//                case "7":
//                  //  printFriends();
//                    break;
////                case "8":
////                    printCommunities();
////                    break;
////                case "9":
////                    printLargestCom();
////                    break;
//                case "10":
//                    return;
//                case "11":
//                    printFriendsAndDate();
//                    break;
//                case "12":
//                    printFriendsByMonth();
//                    break;
//                case "13":
//                    sendMessage();
//                    break;
//                case "14":
//                    sendReply();
//                    break;
//                case "15":
//                    printSortedMessages();
//                    break;
//                case "16":
//                    removeMessage();
//                    break;
//                case "17":
//                    sendRequest();
//                    break;
//                case "18":
//               //     acceptRequest();
//                    break;
//                case "19":
//             //       refuseRequest();
//                    break;
//                case "20":
//                    removeRequest();
//            }
//        }
//
//    }
//}
