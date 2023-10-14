//Chad Huntebrinker
//CS 456
//Project 1 using Best First Search
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class Guided_Search {
    public static void main(String args[]) throws IOException {
        String beginning_input;
        String destination_input;
        Vector<Vector<String>> adjacent_cities = new Vector<Vector<String>>();
        Vector<Vector<String>> city_coordinates = new Vector<Vector<String>>();
        Vector<String> temp_file_storage = new Vector<String>();
        Scanner sc = new Scanner(System.in);
        String adjacencies_file = "src/Adjacencies.txt";
        String coordinates_file = "src/coordinates.txt";
        Scanner scanner1 = new Scanner(new File(adjacencies_file));
        Scanner scanner2 = new Scanner(new File(coordinates_file));

        //Reads in the information from the Adjacencies and coordinates files
        while (scanner1.hasNextLine()) {
            String line = scanner1.nextLine();
            temp_file_storage.add(line);
        }
        for (int i = 0; i < temp_file_storage.size(); ++i) {
            String[] arr = temp_file_storage.get(i).split(" ");
            adjacent_cities.add(i, new Vector<String>());
            for (String word : arr) {
                adjacent_cities.get(i).add(word);
            }
        }
        temp_file_storage.removeAllElements();

        while (scanner2.hasNextLine()) {
            String line = scanner2.nextLine();
            temp_file_storage.add(line);
        }
        for (int i = 0; i < temp_file_storage.size(); ++i) {
            String[] arr = temp_file_storage.get(i).split(" ");
            city_coordinates.add(i, new Vector<String>());
            for (String word : arr) {
                city_coordinates.get(i).add(word);
            }
        }

        System.out.print("Please enter the town you want to start in: ");
        beginning_input = sc.nextLine();
        System.out.print("Please enter the town you want to finish in: ");
        destination_input = sc.nextLine();

        //Check cities are correct and are present in our program
        for (int i = 0; i < city_coordinates.size(); ++i) {
            String temp = city_coordinates.get(i).get(0);
            if (city_coordinates.get(i).get(0).equals(beginning_input)) {
                break;
            } else if (city_coordinates.get(i).get(0) != beginning_input && i == city_coordinates.size() - 1) {
                System.out.print("The city you have entered for the start does not exist in this list. " +
                        "Please check that your spelling is correct or try entering in a different city.");
                System.exit(0);
            }
        }
        for (int i = 0; i < city_coordinates.size(); ++i) {
            String temp = city_coordinates.get(i).get(0);
            if (city_coordinates.get(i).get(0).equals(destination_input)) {
                break;
            } else if (!(city_coordinates.get(i).get(0).equals(destination_input)) && i == city_coordinates.size() - 1) {
                System.out.print("The city you have entered for the destination does not exist in this list. " +
                        "Please check that your spelling is correct or try entering in a different city.");
                System.exit(0);
            }
        }

        find_city_path(adjacent_cities, city_coordinates, beginning_input, destination_input);

    }

    //This function finds the path from the cities
    public static void find_city_path(Vector<Vector<String>> adjacent_cities, Vector<Vector<String>> city_coordinates, String beginning_input,
                                 String destination_input) {

        Vector<String> open_list = new Vector<String>();
        Vector<String> closed_list = new Vector<String>();
        Vector<String> current_city_list = new Vector<String>();
        Vector<String> previous_city_list1 = new Vector<String>();
        Vector<String> previous_city_list2 = new Vector<String>();
        Vector<String> temp_city_list = new Vector<String>();
        Double temp_city_distance;
        String current_city;
        int temp_position = -1;

        //Set up the current city as the beginning and add it to the open list
        current_city = beginning_input;
        open_list.add(beginning_input);

        //This loop will continue to run until the current city is destination city
        do {
            //If there is nothing in the open list, then there is no solution.
            if (open_list.size() == 0) {
                System.out.print("There is no path between these two cities");
            }
            else {
                //current city is first item in the open list and then we remove it from the open list
                current_city = open_list.get(0);
                current_city_list.add(current_city);
                open_list.remove(0);
                closed_list.add(current_city);

                //We use previous_city_list1 and 2 as a sort of cache to keep track of the path of our most recent
                //past two city lists.  It will update the previous_city_list that hasn't been updated the longest (that
                //is, the smallest previous_city_list).
                if(previous_city_list1.size() > previous_city_list2.size() &&
                    previous_city_list1.size() != current_city_list.size()) {
                    previous_city_list2.removeAllElements();
                    for(int i = 0; i < current_city_list.size() - 1; ++i) {
                        previous_city_list2.add(current_city_list.get(i));
                    }
                }
                else {
                    previous_city_list1.removeAllElements();
                    for(int i = 0; i < current_city_list.size() - 1; ++i) {
                        previous_city_list1.add(current_city_list.get(i));
                    }
                }

                //Check to see that we didn't reach a dead end with the previous city and that our current_city
                //is adjacent to the previous city
                if(current_city_list.size() > 2) {
                    boolean checked_adjacencies = false;


                    //Find index of our current city
                    for (int i = 0; i < adjacent_cities.size(); ++i) {
                        if (adjacent_cities.get(i).get(0).equals(current_city)) {
                            temp_position = i;
                            break;
                        }
                    }

                    //This do while loop continues until we're sure that the current city is adjacent to our previous city
                    do {
                        for (int j = 1; j < adjacent_cities.get(temp_position).size(); ++j) {

                            //If we have used either of the previous_city_lists and our current city doesn't equal where
                            //our spot on the adjacent_city list use to be, then that means we have gone back to a previous
                            //version of our city_path.  So, we need to update our index and find what our current city is
                            if(previous_city_list2.size() == 0 && !(current_city.equals(adjacent_cities.get(temp_position).get(0)))) {
                                for (int i = 0; i < adjacent_cities.size(); ++i) {
                                    if (adjacent_cities.get(i).get(0).equals(current_city)) {
                                        temp_position = i;
                                        break;
                                    }
                                }
                            }
                            else if(previous_city_list1.size() == 0 && !(current_city.equals(adjacent_cities.get(temp_position).get(0)))) {
                                for (int i = 0; i < adjacent_cities.size(); ++i) {
                                    if (adjacent_cities.get(i).get(0).equals(current_city)) {
                                        temp_position = i;
                                        break;
                                    }
                                }
                            }

                            //If we are at our starting position, the open list has nothing, and we don't have anything
                            //stored for our previous_city_lists, then there is no solution.
                            if (current_city_list.size() == 1 && open_list.size() == 0 &&
                                (previous_city_list2.size() == 0 && previous_city_list1.size() == 0)) {
                                System.out.print("There is no path between these two cities");
                                System.exit(0);
                            }

                            //Else, if we are at the starting position, we have stuff in our open list, and either
                            //one of our previous_city_lists has stuff
                            else if(current_city_list.size() == 1 && open_list.size() > 0 &&
                                    (previous_city_list2.size() > 0 || previous_city_list1.size() > 0)) {

                                //If both previous_city_lists are not empty, then we need to remove our current
                                //city from the closed_list because we haven't used the newest city.  It was only
                                //selected, not used.
                                if(previous_city_list2.size() != 0 && previous_city_list1.size() != 0) {
                                    for(int k = 0; k < closed_list.size(); ++k) {
                                        if(current_city.equals(closed_list.get(k))) {
                                            closed_list.remove(k);
                                        }
                                    }
                                }

                                //If previous_city_list2 is newer than previous_city_list1, update
                                //the current_city_list with that.
                                if(previous_city_list2.size() >  previous_city_list1.size()) {
                                    current_city_list.removeAllElements();
                                    for(int k = 0; k < previous_city_list2.size(); ++k) {
                                        current_city_list.add(previous_city_list2.get(k));
                                    }
                                    previous_city_list2.removeAllElements();
                                    current_city = current_city_list.get(current_city_list.size() - 1);
                                    j = 0;
                                }
                                //Else if both lists are the same size, then see which list has the last element
                                //as adjacent to the current_city and use that one.
                                else if(previous_city_list1.size() == previous_city_list2.size()) {
                                    for(int l = 0; l < adjacent_cities.get(temp_position).size(); ++l) {

                                        if(adjacent_cities.get(temp_position).get(l).equals(previous_city_list2.get(previous_city_list2.size() - 1))) {

                                            current_city_list.removeAllElements();
                                            for(int k = 0; k < previous_city_list2.size(); ++k) {
                                                current_city_list.add(previous_city_list2.get(k));
                                            }
                                            previous_city_list2.removeAllElements();
                                            current_city = current_city_list.get(current_city_list.size() - 1);
                                            j = 0;
                                            break;
                                        }
                                        else if(l == adjacent_cities.get(temp_position).size() - 1) {
                                            current_city_list.removeAllElements();
                                            for(int k = 0; k < previous_city_list1.size(); ++k) {
                                                current_city_list.add(previous_city_list1.get(k));
                                            }
                                            previous_city_list1.removeAllElements();
                                            current_city = current_city_list.get(current_city_list.size() - 1);
                                            j = 0;
                                        }

                                    }
                                }

                                //Else just use previous_city_list1
                                else {
                                    current_city_list.removeAllElements();
                                    for(int k = 0; k < previous_city_list1.size(); ++k) {
                                        current_city_list.add(previous_city_list1.get(k));
                                    }
                                    previous_city_list1.removeAllElements();
                                    current_city = current_city_list.get(current_city_list.size() - 1);
                                    j = 0;
                                }
                            }

                            //Else if the current_city is adjacent to the previous city, then we're done checking
                            else if (adjacent_cities.get(temp_position).get(j).equals(current_city_list.get(current_city_list.size() - 2))) {
                                checked_adjacencies = true;
                                break;
                            }

                            //Else if we have looked through all the adjacencies and the current city isn't adjacent to any of them, remove it from
                            //the current_city_list.
                            else if (!(adjacent_cities.get(temp_position).get(j).equals(current_city_list.get(current_city_list.size() - 2))) &&
                                    j == adjacent_cities.get(temp_position).size() - 1) {
                                current_city_list.removeElementAt(current_city_list.size() - 2);
                                break;
                            }
                        }

                        //If we have checked our adjacencies, then we are done.
                        if (checked_adjacencies) {
                            break;
                        }
                    } while(checked_adjacencies == false);
                }

                //If our current city is the destination then add it to our current list and print the city path.
                if (current_city.equals(destination_input)) {
                    print_city_path(current_city_list);
                    break;
                }
                //Else, we need to see what's adjacent to the current city and find the best one to use.
                else {
                    for (int i = 0; i < city_coordinates.size(); i++) {
                        //If we found the city in our adjacent city list
                        if (adjacent_cities.get(i).get(0).equals(current_city)) {
                            //Get all the adjacent cities in our temp_city vector and make sure they aren't on the closed list
                            for (int j = 1; j < adjacent_cities.get(i).size(); j++) {

                                for (int l = 0; l < closed_list.size(); l++) {

                                    if(!(adjacent_cities.get(i).get(j).equals(closed_list.get(l))) && l == closed_list.size() - 1) {
                                        temp_city_list.add(adjacent_cities.get(i).get(j));
                                    }

                                    else if(adjacent_cities.get(i).get(j).equals(closed_list.get(l))) {
                                        break;
                                    }
                                }
                            }
                            //Find their distances, see which one will get us closest to the goal (including what's in the
                            //open list, and then store it in the open_list according to what's most promising.
                            for (int j = 0; j < temp_city_list.size(); ++j) {
                                temp_city_distance = get_distance(city_coordinates, temp_city_list.get(j), destination_input);

                                for(int k = 0; k < open_list.size(); ++k){

                                    //If the temp_city is closer than the one in the open list and not in the closed list
                                    if(temp_city_distance < get_distance(city_coordinates, open_list.get(k), destination_input)){
                                        open_list.add(k, temp_city_list.get(j));
                                        break;
                                    }

                                    //Or if were at the end of the list and it's still further, then put it at the end
                                    else if(temp_city_distance > get_distance(city_coordinates, open_list.get(k), destination_input) &&
                                            k == open_list.size() - 1) {
                                        open_list.add(temp_city_list.get(j));
                                        break;
                                    }
                                }

                                //If there's nothing in the open list, just insert it in.
                                if(open_list.size() == 0) {
                                    open_list.add(temp_city_list.get(j));
                                }
                            }
                            temp_city_list.clear();
                            break;
                        }
                    }
                }
            }
        } while (current_city != destination_input);
    }

    //Displays the city path.
    public static void print_city_path(Vector<String> citypath) {
        System.out.print(citypath.get(0));

        for(int i = 1; i < citypath.size(); ++i) {
            System.out.print(" -> ");
            System.out.print(citypath.get(i));
        }
        System.out.print("\n");
    }

    //Calculates the distance between two cities using the distance formula.
    public static Double get_distance(Vector<Vector<String>> city_coordinates, String city1, String city2) {
        Double total_distance = 0.0;
        Double x_coordinate_for_city1 = 0.0, y_coordinate_for_city1 = 0.0;
        Double x_coordinate_for_city2 = 0.0, y_coordinate_for_city2 = 0.0;
        boolean city1_done = false;
        boolean city2_done = false;

        for (int i = 0; i < city_coordinates.size(); ++i) {
            if (city_coordinates.get(i).get(0).equals(city1)) {
                x_coordinate_for_city1 = Double.parseDouble(city_coordinates.get(i).get(1));
                y_coordinate_for_city1 = Double.parseDouble(city_coordinates.get(i).get(2));
                city1_done = true;
            }
            if (city_coordinates.get(i).get(0).equals(city2)) {
                x_coordinate_for_city2 = Double.parseDouble(city_coordinates.get(i).get(1));
                y_coordinate_for_city2 = Double.parseDouble(city_coordinates.get(i).get(2));
                city2_done = true;
            }
            if (city1_done && city2_done) {
                break;
            }
        }

        total_distance = Math.pow((x_coordinate_for_city1 - x_coordinate_for_city2), 2) +
                Math.pow((y_coordinate_for_city1 - y_coordinate_for_city2), 2);
        total_distance = Math.sqrt(total_distance);

        return total_distance;
    }

}