package ra.presentation;

import ra.business.ContactManager;

import java.util.Scanner;

public class ContactApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ContactManager contactManager = new ContactManager();

        do {
            System.out.println("\n----------------------- Contact Book Menu --------------------\n" +
                    "1. Hiển thị danh sách liên hệ\n" +
                    "2. Thêm các liên hệ mới\n" +
                    "3. Chỉnh sửa thông tin liên hệ\n" +
                    "4. Xóa liên hệ\n" +
                    "5. Tìm kiếm liên hệ \n" +
                    "6. Thống kê số lượng liên hệ theo mức độ quan trọng\n" +
                    "7. Thống kê số lượng liên hệ theo giới tính\n" +
                    "8. Sắp xếp liên hệ theo tên(a-z / z-a)\n" +
                    "9. Thoát chương trình\n" +
                    "----------------------------------------------------------------------\n");
            System.out.print("Lựa chọn của bạn: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    contactManager.displayContactList();
                    break;
                case 2:
                    contactManager.addContact(scanner);
                    break;
                case 3:
                    contactManager.updateContact(scanner);
                    break;
                case 4:
                    contactManager.deleteContact(scanner);
                    break;
                case 5:
                    contactManager.searchContactByEmail(scanner);
                    break;
                case 6:
                    contactManager.countContactGroupByRating();
                    break;
                case 7:
                    contactManager.countContactGroupByGender();
                    break;
                case 8:
                    contactManager.sortContactByName(scanner);
                    break;
                case 9:
                    System.out.println("Cảm ơn bạn đã sử dụng chương trình của tôi. Hẹn găp lại!");
                    System.exit(0);
                default:
                    System.err.println("Vui lòng nhập vào các số trong khoảng 1-9");
            }
        } while (true);

    }
}
