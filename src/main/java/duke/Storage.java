package duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * deals with loading tasks from the file and saving tasks in the file
 *
 * @author eugeneleong
 * @version 1.0
 */

public class Storage {

    public final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Responsible for loading all the pre-existing tasks from file
     * @return ArrayList<Task></Task>
     * @throws FileNotFoundException if file cannot be found
     */
    public ArrayList<Task> load() throws FileNotFoundException {
        try {
            ArrayList<Task> ls = new ArrayList<>();
            File file = new File(filePath);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String input = sc.nextLine();
                String commandType = Parser.getCommandType(input);
                if ((commandType.equals("MARK"))) {
                    int markIdx = Integer.parseInt(input.substring(5)) - 1;
                    ls.get(markIdx).markIsDone();
                } else if ((commandType.equals("UNMARK"))) {
                    int unmarkIdx = Integer.parseInt(input.substring(7)) - 1;
                    ls.get(unmarkIdx).unmarkIsDone();
                } else if (commandType.equals("TODO")) {
                    ls.add(new ToDo(input.substring(5)));
                } else if (commandType.equals("DEADLINE")) {
                    String dlAction = input.substring(9, input.indexOf("/") - 1);
                    ls.add(new Deadline(dlAction, Parser.formatEventTime(input)));
                } else if (commandType.equals("EVENT")) {
                    String eAction = input.substring(6, input.indexOf("/") - 1);
                    ls.add(new Event(eAction, Parser.formatEventTime(input)));
                } else if (commandType.equals("DELETE")) {
                    int deleteIdx = Integer.parseInt(input.substring(7)) - 1;
                    ls.remove(deleteIdx);
                }
            }
            return ls;
        } catch (FileNotFoundException fnfe) {
            throw new FileNotFoundException("File cannot be found!");
        }
    }

    /**
     * Responsible for saving the user's input into the file
     * @param text to save
     * @throws IOException if the user's input cannot be saved
     */
    public void saveToFile(String text) throws IOException {
        try {
            Files.write(Paths.get(filePath), ("\n" + text).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IOException("Error occurred while appending text to file.");
        }
    }

}
