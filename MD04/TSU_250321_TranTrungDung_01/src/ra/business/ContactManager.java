package ra.business;

import ra.entity.Contact;

import java.util.Scanner;

public class ContactManager {
    public static Contact[] contacts = new Contact[100];
    public static int currentIndex = 0;

    public void displayContactList() {
        System.out.println("Danh sách liên hệ: ");
        for (int i = 0; i < currentIndex; i++) {
            contacts[i].displayDta();
        }
    }

    public void addContact(Scanner scanner) {
        System.out.print("Nhập vào số contact muốn thêm mới: ");
        int countNewContact = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < countNewContact; i++) {
            contacts[currentIndex] = new Contact();
            contacts[currentIndex].inputData(scanner);
            currentIndex++;
            System.out.println("------------");
        }
        System.out.println("Thêm mới thành công!");
    }

    public void updateContact(Scanner scanner) {
        System.out.print("Nhập vào mã ID contact muốn cập nhật: ");
        int idUpdate = Integer.parseInt(scanner.nextLine());
        int indexUpdate = findIndexById(idUpdate);
        if (indexUpdate != -1) {
            boolean isExit = false;
            do {
                System.out.println("1. Cập nhật họ tên");
                System.out.println("2. Cập nhật email");
                System.out.println("3. Cập nhật số điện thoại");
                System.out.println("4. Cập nhật giới tính");
                System.out.println("5. Cập nhật địa chỉ");
                System.out.println("6. Cập nhật rating");
                System.out.println("7. Cập nhật ghi chú");
                System.out.println("8. Thoát cập nhật");
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        contacts[indexUpdate].setName(contacts[indexUpdate].inputName(scanner));
                        break;
                    case 2:
                        contacts[indexUpdate].setEmail(contacts[indexUpdate].inputEmail(scanner));
                        break;
                    case 3:
                        contacts[indexUpdate].setPhone(contacts[indexUpdate].inputPhone(scanner));
                        break;
                    case 4:
                        contacts[indexUpdate].setSex(contacts[indexUpdate].inputSex(scanner));
                        break;
                    case 5:
                        contacts[indexUpdate].setAddress(contacts[indexUpdate].inputAddress(scanner));
                        break;
                    case 6:
                        contacts[indexUpdate].setRating(contacts[indexUpdate].inputRating(scanner));
                        break;
                    case 7:
                        contacts[indexUpdate].setNote(contacts[indexUpdate].inputNote(scanner));
                        break;
                    case 8:
                        isExit = true;
                        break;
                    default:
                        System.err.println("Vui lòng nhập số trong khoảng 1-8");
                }
                System.out.println("Đã cập nhật xong!");
            } while (!isExit);
        }
        System.err.println("Mã ID này không tồn tại!");
    }

    public void deleteContact(Scanner scanner) {
        System.out.print("Nhập mã ID contact muốn xóa: ");
        int idDelete = Integer.parseInt(scanner.nextLine());
        int indexDelete = findIndexById(idDelete);
        if (indexDelete == -1) {
            System.out.println("Thông tin liên hệ muốn xóa: ");
            contacts[idDelete].displayDta();
            System.out.print("Bạn có chắc chắn muốn xóa liên hệ này? Nhập vào 'y' nếu muốn xóa. Nếu không nhập phím bất kỳ");
            char deleteChoice = scanner.nextLine().charAt(0);
            if (deleteChoice == 'y') {
                for (int i = indexDelete; i < currentIndex - 1; i++) {
                    contacts[i] = contacts[i + 1];
                }
                contacts[currentIndex - 1] = null;
                currentIndex--;
                System.out.println("Xóa thành công!");
            }
        } else {
            System.err.println("Mã ID này không tồn tại!");
        }
    }

    public void searchContactByEmail(Scanner scanner) {
        System.out.print("Nhập vào email của contact bạn muốn tìm: ");
        String emailSearch = scanner.nextLine();
        System.out.println("Liên hệ bạn muốn tìm: ");
        for (int i = 0; i < currentIndex; i++) {
            if (contacts[i].getEmail().equals(emailSearch.trim())) {
                contacts[i].displayDta();
            }
        }
    }

    public void countContactGroupByRating() {
        int countRating1 = 0;
        int countRating2 = 0;
        int countRating3 = 0;
        int countRating4 = 0;
        int countRating5 = 0;
        for (int i = 0; i < currentIndex; i++) {
            switch (contacts[i].getRating()) {
                case 1:
                    countRating1++;
                    break;
                case 2:
                    countRating2++;
                    break;
                case 3:
                    countRating3++;
                    break;
                case 4:
                    countRating4++;
                    break;
                default:
                    countRating5++;
            }
        }
        System.out.println("Mức độ 'không quan trọng': " + countRating1);
        System.out.println("Mức độ 'ít tương tác': " + countRating2);
        System.out.println("Mức độ 'bình thường': " + countRating3);
        System.out.println("Mức độ 'thân thiết': " + countRating4);
        System.out.println("Mức độ 'VIP': " + countRating5);
    }

    public void countContactGroupByGender() {
        int countMale = 0;
        int countFemale = 0;
        for (int i = 0; i < currentIndex; i++) {
            if (contacts[i].isSex()) {
                countMale++;
            }
            countFemale++;
        }
        System.out.println("Giới tính NAM: " + countMale);
        System.out.println("Giới tính NỮ: " + countFemale);
    }

    public void sortContactByName(Scanner scanner) {
        System.out.println("Bạn muốn sắp xếp liên hệ theo tên từ A-Z. Nhập vào 1");
        System.out.println("Bạn muốn sắp xếp liên hệ theo tên từ Z-A. Nhập vào 2");
        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1:
                for (int i = 0; i < currentIndex - 1; i++) {
                    for (int j = i + 1; j < currentIndex; j++) {
                        if (contacts[i].getName().compareTo(contacts[j].getName()) > 0) {
                            Contact temp = contacts[i];
                            contacts[i] = contacts[j];
                            contacts[j] = temp;
                        }
                    }
                }
                System.out.println("Sắp xếp thành công theo tên từ A-Z");
                break;
            default:
                for (int i = 0; i < currentIndex - 1; i++) {
                    for (int j = i + 1; j < currentIndex; j++) {
                        if (contacts[i].getName().compareTo(contacts[j].getName()) < 0) {
                            Contact temp = contacts[i];
                            contacts[i] = contacts[j];
                            contacts[j] = temp;
                        }
                    }
                }
                System.out.println("Sắp xếp thành công theo tên từ Z-A");
        }
    }

    public int findIndexById(int id) {
        for (int i = 0; i < currentIndex; i++) {
            if (contacts[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
