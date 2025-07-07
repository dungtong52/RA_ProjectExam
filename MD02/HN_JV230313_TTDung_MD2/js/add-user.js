const users = JSON.parse(localStorage.getItem('userList'));
const currentUser = JSON.parse(localStorage.getItem('currentUser'));

const form = document.getElementById('add-new-user-form');
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
const addBtn = document.querySelector('.add-btn');

const msg = document.getElementById('msg');
const addToast = document.getElementById('add-toast');
const addError = document.getElementById('add-error');
const emailUsernamePasswordEmpty = document.querySelector(
  '.email-username-password-empty'
);
const emailExist = document.querySelector('.email-exist');
const emailError = document.querySelector('.email-error');
const passwordMinLengthError = document.querySelector(
  '.password-min-length-error'
);
const passwordNumberRequiredError = document.querySelector(
  '.password-number-required-error'
);
const passwordUppercaseLowercaseError = document.querySelector(
  '.password-uppercase-lowercase-error'
);
const roleNotAdmin = document.querySelector('.role-not-admin');

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

// Kiểm tra username, password rỗng
function isEmpty(email, username, password) {
  if (username === '' || password === '' || email === '') {
    msg.classList.add('show');
    addError.classList.remove('hidden');
    emailUsernamePasswordEmpty.classList.remove('hidden');
  }
  return username !== '' && password !== '' && email !== '';
}
// Kiểm tra email đúng định dạng chưa
function isValidationEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const validateEmail = emailRegex.test(email);

  if (!validateEmail) {
    addError.classList.remove('hidden');
    emailError.classList.remove('hidden');
    msg.classList.add('show');
  }
  return validateEmail;
}
// Kiểm tra tồn tại email
function isExistEmail(email) {
  const isDuplicate = users.some((u) => u.email === email);
  if (isDuplicate) {
    addError.classList.remove('hidden');
    emailExist.classList.remove('hidden');
    msg.classList.add('show');
  }
  return !isDuplicate;
}
// Kiểm tra password
function isEditError(password) {
  const minLength = 8;
  const hasUppercase = /[A-Z]/.test(password);
  const hasLowercase = /[a-z]/.test(password);
  const hasNumber = /[0-9]/.test(password);

  let isValid = true;

  if (password.length < minLength) {
    addError.classList.remove('hidden');
    passwordMinLengthError.classList.remove('hidden');
    msg.classList.add('show');
    isValid = false;
  }
  if (!hasUppercase || !hasLowercase) {
    addError.classList.remove('hidden');
    passwordUppercaseLowercaseError.classList.remove('hidden');
    msg.classList.add('show');
    isValid = false;
  }
  if (!hasNumber) {
    addError.classList.remove('hidden');
    passwordNumberRequiredError.classList.remove('hidden');
    msg.classList.add('show');
    isValid = false;
  }
  return isValid;
}

// Reset các thông báo lỗi
function resetErrorMsg() {
  addToast.classList.add('hidden');
  addError.classList.add('hidden');
  emailUsernamePasswordEmpty.classList.add('hidden');
  emailExist.classList.add('hidden');
  emailError.classList.add('hidden');
  passwordMinLengthError.classList.add('hidden');
  passwordUppercaseLowercaseError.classList.add('hidden');
  passwordNumberRequiredError.classList.add('hidden');
  msg.classList.remove('show');
}

// Role admin mới cho phép add user
function isAdmin(role) {
  if (role !== 'admin') {
    addError.classList.remove('hidden');
    roleNotAdmin.classList.remove('hidden');
    msg.classList.add('show');
  }
  return role === 'admin';
}

// Gắn sự kiện cho button add
form.addEventListener('submit', (e) => {
  e.preventDefault();
  //gắn giá trị nhập
  const usernameInput = username.value.trim();
  const emailInput = email.value;
  const passwordInput = password.value.trim();
  const roleInput = role.value;
  const birthdayInput = birthday.value;
  const statusInput = getValueRadio();
  const descriptionInput = description.value;

  // kiểm tra
  if (!isEmpty(emailInput, usernameInput, passwordInput)) return;
  if (!isValidationEmail(emailInput)) return;
  if (!isExistEmail(emailInput)) return;
  if (!isEditError(passwordInput)) return;
  if (!isAdmin(currentUser.role)) return;

  // Create new user
  const user = {
    usercode: uniqueUsercode(),
    username: usernameInput,
    email: emailInput,
    password: passwordInput,
    role: roleInput,
    birthday: birthdayInput,
    status: statusInput,
    description: descriptionInput,
  };
  users.push(user);
  localStorage.setItem('userList', JSON.stringify(users));
  addToast.classList.remove('hidden');
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

// tạo Id unique
function uniqueUsercode() {
  const string = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
  let code;

  do {
    const charRandom = string[Math.floor(Math.random() * string.length)];
    const numberRandom = Math.floor(Math.random() * 10);
    code = `${charRandom}${numberRandom}${numberRandom}`;
  } while (users.some((u) => u.usercode === uniqueUsercode));
  return code;
}

// Reset thông báo khi nhập vào username và input
email.addEventListener('input', resetErrorMsg);
username.addEventListener('input', resetErrorMsg);
password.addEventListener('input', resetErrorMsg);
