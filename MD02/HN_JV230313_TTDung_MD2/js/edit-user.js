const userCurrentEdit = JSON.parse(sessionStorage.getItem('userEdit'));
const users = JSON.parse(localStorage.getItem('userList'));

const form = document.getElementById('edit-user-form');
const usercode = document.getElementById('user-code');
const username = document.getElementById('username');
const email = document.getElementById('email');
const password = document.getElementById('password');
const role = document.getElementById('role');
const birthday = document.getElementById('dob');
const radios = document.getElementsByName('status');
const description = document.querySelector('.description-text-input');
const showPassword = document.querySelector('.fa-eye');
const hidePassword = document.querySelector('.fa-eye-slash');

const backBtn = document.querySelector('.back-btn');
const saveBtn = document.querySelector('.save-btn');

const msg = document.getElementById('msg');
const editToast = document.getElementById('edit-toast');
const editError = document.getElementById('edit-error');
const usernameAndPasswordEmpty = document.querySelector(
  '.username-and-password-empty'
);
const passwordMinLengthError = document.querySelector(
  '.password-min-length-error'
);
const passwordNumberRequiredError = document.querySelector(
  '.password-number-required-error'
);
const passwordUppercaseLowercaseError = document.querySelector(
  '.password-uppercase-lowercase-error'
);

// Lấy value từ ô radio user nhập
function getValueRadio() {
  let selectedValue = '';
  for (let i = 0; i < radios.length; i++) {
    if (radios[i].checked) {
      selectedValue = radios[i].value;
    }
  }
  return selectedValue;
}
// Lấy data value về ô radio
function setValueRadio(value) {
  for (let i = 0; i < radios.length; i++) {
    if (radios[i].value === value) {
      radios[i].checked = true;
    }
  }
}
// Kiểm tra username, password rỗng
function isEmpty(username, password) {
  if (username === '' || password === '') {
    msg.classList.add('show');
    editError.classList.remove('hidden');
    usernameAndPasswordEmpty.classList.remove('hidden');
  }
  return username !== '' && password !== '';
}
// Kiểm tra password
function isEditError(password) {
  const minLength = 8;
  const hasUppercase = /[A-Z]/.test(password);
  const hasLowercase = /[a-z]/.test(password);
  const hasNumber = /[0-9]/.test(password);

  let isValid = true;

  if (password.length < minLength) {
    editError.classList.remove('hidden');
    passwordMinLengthError.classList.remove('hidden');
    msg.classList.add('show');
    isValid = false;
  }
  if (!hasUppercase || !hasLowercase) {
    editError.classList.remove('hidden');
    passwordUppercaseLowercaseError.classList.remove('hidden');
    msg.classList.add('show');
    isValid = false;
  }
  if (!hasNumber) {
    editError.classList.remove('hidden');
    passwordNumberRequiredError.classList.remove('hidden');
    msg.classList.add('show');
    isValid = false;
  }
  return isValid;
}
// Reset các thông báo lỗi
function resetErrorMsg() {
  editToast.classList.add('hidden');
  editError.classList.add('hidden');
  usernameAndPasswordEmpty.classList.add('hidden');
  passwordMinLengthError.classList.add('hidden');
  passwordUppercaseLowercaseError.classList.add('hidden');
  passwordNumberRequiredError.classList.add('hidden');
  msg.classList.remove('show');
}

// Nhập dữ liệu người dùng muốn sửa từ sessionStorage
window.onload = () => {
  if (!userCurrentEdit) return;
  // giá trị cũ được cập nhật vào ô input
  usercode.value = userCurrentEdit.usercode;
  username.value = userCurrentEdit.username;
  email.value = userCurrentEdit.email;
  password.value = userCurrentEdit.password;
  role.value = userCurrentEdit.role;
  birthday.value = userCurrentEdit.birthday;
  setValueRadio(userCurrentEdit.status);
  description.value = userCurrentEdit.description;
};
// Gắn sự kiện cho button save
form.addEventListener('submit', (e) => {
  e.preventDefault();
  //gắn lại giá trị
  const usercodeValue = usercode.value;
  const usernameInput = username.value.trim();
  const emailValue = email.value;
  const passwordInput = password.value.trim();
  const roleInput = role.value;
  const birthdayInput = birthday.value;
  const statusInput = getValueRadio();
  const descriptionInput = description.value;
  if (!isEmpty(usernameInput, passwordInput)) return;
  if (!isEditError(passwordInput)) return;

  const index = users.findIndex((u) => u.usercode === usercodeValue);
  users[index] = {
    usercode: usercodeValue,
    username: usernameInput,
    email: emailValue,
    password: passwordInput,
    role: roleInput,
    birthday: birthdayInput,
    status: statusInput,
    description: descriptionInput,
  };

  localStorage.setItem('userList', JSON.stringify(users));
  editToast.classList.remove('hidden');
  msg.classList.add('show');

  // Chuyển trang dashboard
  setTimeout(() => {
    window.location.href = './dashboard.html';
  }, 800);
});

// Gắn sự kiện cho button back
backBtn.addEventListener('click', () => {
  history.back();
});

// hide / show password
hidePassword.onclick = function () {
  password.type = 'password';
  hidePassword.style.display = 'none';
  showPassword.style.display = 'block';
};
showPassword.onclick = function () {
  password.type = 'text';
  hidePassword.style.display = 'block';
  showPassword.style.display = 'none';
};

// Reset thông báo khi nhập vào username và input
username.addEventListener('input', resetErrorMsg);
password.addEventListener('input', resetErrorMsg);
