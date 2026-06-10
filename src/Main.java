import ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}




// import ui.HomeFrame;
// import model.User;
// import javax.swing.*;

// public class Main {

//     public static void main(String[] args) {

//         SwingUtilities.invokeLater(() -> {

//             // Create fake demo user
//             User demoUser = new User();

//             demoUser.setId(1);
//             demoUser.setName("Ayush");

//             // Open HomeFrame directly
//             new HomeFrame(demoUser).setVisible(true);
//         });
//     }
// }

