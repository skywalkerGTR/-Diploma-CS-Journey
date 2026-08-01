#include <iostream>
#include <string>
using namespace std;

const int TOTAL_ROOMS = 5;

// Room booking name list
string roomName[TOTAL_ROOMS];

// Room price list (per night)
int roomPrice[TOTAL_ROOMS] = {120, 150, 180, 200, 250};

// Nights booked for each room
int nightsBooked[TOTAL_ROOMS] = {0, 0, 0, 0, 0};

// -------------------- SHOW MENU --------------------
void showMenu() {
    cout << "=====================================================" << endl;
    cout << "              HOMESTAY BOOKING SYSTEM  \n";
    cout << "=====================================================" << endl;
    cout << "1. View Available Rooms" << endl;
    cout << "2. Book a Room" << endl;
    cout << "3. Cancel Booking" << endl;
    cout << "4. View All Bookings" << endl;
    cout << "5. Exit" << endl;
    cout << "Enter your choice:" << endl;
}

// -------------------- VIEW AVAILABLE ROOMS --------------------
void viewAvailableRooms() {
    cout << "\nAvailable Rooms (with Price per Night):\n";
    for (int i = 0; i < TOTAL_ROOMS; i++) {
        if (roomName[i] == "") {
            cout << "Room " << (i + 1)
                 << " - RM" << roomPrice[i] 
                 << " per night (Available)\n";
        }
    }
}

// -------------------- BOOK A ROOM --------------------
void bookRoom() {
    int roomNumber;
    string name;
    int nights;

    cout << "\nEnter Room Number to Book (1-5): ";
    cin >> roomNumber;

    if (roomNumber < 1 || roomNumber > TOTAL_ROOMS) {
    cout << "Invalid room number!" << endl;
    return;
    }

    if (roomName[roomNumber - 1] != "") {
    cout << "Sorry, this room is already booked!" << endl;
    return;
    }

    cout << "Enter customer name (one word): ";
    cin >> name;

    cout << "Enter number of nights: ";
    cin >> nights;

    roomName[roomNumber - 1] = name;
    nightsBooked[roomNumber - 1] = nights;

    int total = roomPrice[roomNumber - 1] * nights;

	cout << "=====================================================" << endl;
    cout << "Room " << roomNumber << " successfully booked for " << name << "!" << endl;
    cout << "Price per night : RM" << roomPrice[roomNumber - 1] << endl;
    cout << "Nights booked   : " << nights << endl;
    cout << "TOTAL PRICE     : RM" << total << endl;
    cout << "=====================================================" << endl;
}

// -------------------- CANCEL A BOOKING --------------------
void cancelBooking() {
    int roomNumber;

    cout << "\nEnter Room Number to Cancel (1-5): ";
    cin >> roomNumber;

    if (roomNumber < 1 || roomNumber > TOTAL_ROOMS) {
    cout << "Invalid room number!\n";
    return;
    }

    if (roomName[roomNumber - 1] == "") {
    cout << "This room is not booked yet." << endl;
    return;
    }

    cout << "Booking for " << roomName[roomNumber - 1] << " has been cancelled." << endl;

    roomName[roomNumber - 1] = "";
    nightsBooked[roomNumber - 1] = 0;
}

// -------------------- VIEW ALL BOOKINGS --------------------
void viewAllBookings() {
    cout << "\nCurrent Room Bookings (with Total Price):" << endl;
    for (int i = 0; i < TOTAL_ROOMS; i++) {
        if (roomName[i] == "") {
            cout << "Room " << (i + 1)
                 << ": EMPTY - RM" << roomPrice[i] << " per night\n";
        } 
        else {
            int total = roomPrice[i] * nightsBooked[i];
            cout << "Room " << (i + 1)
                 << ": " << roomName[i]
                 << " | Nights: " << nightsBooked[i]
                 << " | Total: RM" << total << endl;
        }
    }
}

// -------------------- MAIN PROGRAM --------------------
int main() {
    // Initialize all rooms as empty
    for (int i = 0; i < TOTAL_ROOMS; i++) {
        roomName[i] = "";
        nightsBooked[i] = 0;
    }

    int choice;

    do {
        showMenu();
        cin >> choice;

        if (choice == 1) {
        viewAvailableRooms();
        }
        else if (choice == 2) {
        bookRoom();
        }
        else if (choice == 3) {
        cancelBooking();
        }
        else if (choice == 4) {
        viewAllBookings();
        }
        else if (choice == 5) {
        cout << "Exiting program... Thank you!" << endl;
        }
        else {
        cout << "Invalid choice. Please try again." << endl;
        }

    } while (choice != 5);

    return 0;
}
