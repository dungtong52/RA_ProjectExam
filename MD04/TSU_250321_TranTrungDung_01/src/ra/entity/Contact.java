package ra.entity;

import ra.business.ContactManager;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Contact implements IContact {
    private int id;
    private String name;
    private String email;
    private String phone;
    private boolean sex;
    private String address;
    private byte rating;
    private String note;

    public Contact() {
    }

    public Contact(int id, String name, String email, String phone, boolean sex, String address, byte rating, String note) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.sex = sex;
        this.address = address;
        this.rating = rating;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte getRating() {
        return rating;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public void inputData(Scanner scanner) {
        this.id = inputId();
        this.name = inputName(scanner);
        this.email = inputEmail(scanner);
        this.phone = inputPhone(scanner);
        this.sex = inputSex(scanner);
        this.address = inputAddress(scanner);
        this.rating = inputRating(scanner);
        this.note = inputNote(scanner);
    }

    public int inputId() {
        // Tự tăng, duy nhất, không được sửa sau khi tạo
        if (ContactManager.currentIndex == 0) {
            ContactManager.currentIndex = 1;
        }
        int maxIndex = ContactManager.contacts[0].getId();
        for (int i = 0; i < ContactManager.currentIndex; i++) {
            if (ContactManager.contacts[i].getId() > maxIndex) {
                maxIndex = ContactManager.contacts[i].getId();
            }
        }
        return maxIndex + 1;
    }

    public String inputName(Scanner scanner) {
        // Không được để trống, tối đa 50 ký tự
        do {
            System.out.print("Nhập vào họ và tên liên hệ: ");
            String name = scanner.nextLine();
            if (!isEmpty(name)) {
                if (name.length() < 50) {
                    return name;
                } else {
                    System.err.println("Họ tên chỉ được tối đa 50 ký tự");
                }
            } else {
                System.err.println("Vui lòng nhập vào họ và tên liên hệ!");
            }
        } while (true);
    }

    public String inputEmail(Scanner scanner) {
        // Không để trống, đúng định dạng email, duy nhất
        String emailRegex = "[a-zA-Z0-9.]+@[a-zA-Z]+\\.[a-z]+";
        do {
            System.out.print("Nhập vào địa chỉ email: ");
            String email = scanner.nextLine();
            if (!isEmpty(email)) {
                if (Pattern.matches(emailRegex, email)) {
                    boolean isExist = false;
                    for (int i = 0; i < ContactManager.currentIndex; i++) {
                        if (ContactManager.contacts[i].getEmail().equals(email)) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        return email;
                    } else {
                        System.err.println("Email này đã tồn tại. Nhập vào email khác!");
                    }
                } else {
                    System.err.println("Email không đúng định dạng!");
                }
            } else {
                System.err.println("Vui lòng nhập vào địa chỉ email!");
            }
        } while (true);
    }

    public String inputPhone(Scanner scanner) {
        // Không để trống, chỉ chứa chữ số 0-9, có 10 hoặc 11 chữ số, bắt đầu từ số 0, duy nhất
        String phoneRegex = "[0][0-9]{9,10}";
        do {
            System.out.print("Nhập vào số điện thoại liên hệ: ");
            String phone = scanner.nextLine();
            if (!isEmpty(phone)) {
                if (Pattern.matches(phoneRegex, phoneRegex)) {
                    boolean isExists = false;
                    for (int i = 0; i < ContactManager.currentIndex; i++) {
                        if (!ContactManager.contacts[i].getPhone().equals(phone)) {
                            isExists = true;
                            break;
                        }
                    }
                    if (!isExists) {
                        return phone;
                    } else {
                        System.err.println("Số điện thoại đã tồn tại!");
                    }
                }
            } else {
                System.err.println("Vui lòng nhập vào số điện thoại liên hệ!");
            }
        } while (true);
    }

    public boolean inputSex(Scanner scanner) {
        // Không để trống
        do {
            System.out.print("Nhập vào giới tính (true là Nam | false là Nữ): ");
            String sex = scanner.nextLine();
            if (!isEmpty(sex)) {
                if (sex.equalsIgnoreCase("true") || sex.equalsIgnoreCase("false")) {
                    return Boolean.parseBoolean(sex);
                } else {
                    System.err.println("Nhập vào không đúng định dạng!");
                }
            } else {
                System.err.println("Vui lòng nhập vào giới tính!");
            }
        } while (true);
    }

    public String inputAddress(Scanner scanner) {
        // Có thể để trống, tối đa 100 ký tự
        final int MAX_LENGTH_ADDRESS = 100;
        System.out.print("Nhập vào địa chỉ: ");
        String address = scanner.nextLine();
        if (!isEmpty(address) && address.length() > MAX_LENGTH_ADDRESS) {
            System.err.println("Địa chỉ có độ dài ít hơn 100 ký tự!");
            return "";
        }
        return address;
    }

    public byte inputRating(Scanner scanner) {
        // Không trống, từ 1 đến 5
        do {
            System.out.print("Nhập vào đánh giá mức độ quan trọng:  ");
            byte rating = Byte.parseByte(scanner.nextLine());
            if (scanner.hasNextByte()) {
                if (rating >= 1 && rating <= 5) {
                    return rating;
                } else {
                    System.err.println("Vui lòng nhập số nguyên từ 1-5");
                }
            } else {
                System.err.println("Vui lòng nhập số nguyên từ 1-5");
            }
        } while (true);
    }

    public String inputNote(Scanner scanner) {
        // Có thể để trống, tối đa 100 ký tự
        final int MAX_LENGTH_NOTE = 100;
        System.out.print("Nhập vào ghi chú thêm về liên hệ: ");
        String note = scanner.nextLine();
        if (!isEmpty(note) && note.length() > MAX_LENGTH_NOTE) {
            System.err.println("Ghi chú có độ dài ít hơn 100 ký tự!");
            return "";
        }
        return note;
    }

    public boolean isEmpty(String data) {
        if (data == null && data.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void displayDta() {
        System.out.printf("ID: %d - Name: %s - Email: %s\n", this.id, this.name, this.email);
        System.out.printf("Phone: %s - Sex: %s - Address: %s\n", this.phone, this.sex ? "Nam" : "Nữ", this.address);
        System.out.printf("Rating: %s - Note: %s\n", convertRating(this.rating), this.note);
    }

    public String convertRating(int data) {
        return switch (data) {
            case 1 -> "không quan trọng";
            case 2 -> "ít tương tác";
            case 3 -> "bình thường";
            case 4 -> "thân thiết";
            case 5 -> "VIP";
            default -> "Vui lòng nhập vào các số trong khoảng 1-5";
        };
    }
}

