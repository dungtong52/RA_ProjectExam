const users = JSON.parse(localStorage.getItem('userList')) || [];

const form = document.getElementById('sign-up-form');
const email = document.getElementById('email');
const username = document.getElementById('username');
const password = document.getElementById('password');
const showPassword = document.querySelector('.fa-eye');
const hidePassword = document.querySelector('.fa-eye-slash');

const msg = document.getElementById('msg');
const signUpValidation = document.getElementById('sign-up-validation');
const signUpToast = document.getElementById('sign-up-toast');
const signUpError = document.getElementById('sign-up-error');
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

// Kiểm tra email, username, password rỗng
function isEmpty(email, username, password) {
  if (email === '' || username === '' || password === '') {
    signUpValidation.classList.remove('hidden');
  }
  return email !== '' && username !== '' && password !== '';
}

// Kiểm tra email đúng định dạng chưa
function isValidationEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const validateEmail = emailRegex.test(email);

  if (!validateEmail) {
    signUpError.classList.remove('hidden');
    emailError.classList.remove('hidden');
    msg.classList.add('show');
  }
  return validateEmail;
}
// Kiểm tra tồn tại email
function isExistEmail(email) {
  const isDuplicate = users.some((u) => u.email === email);
  if (isDuplicate) {
    signUpError.classList.remove('hidden');
    emailExist.classList.remove('hidden');
    msg.classList.add('show');
  }
  return !isDuplicate;
}
// Kiểm tra password
function isPasswordValid(password) {
  const minLength = 8;
  const hasUppercase = /[A-Z]/.test(password);
  const hasLowercase = /[a-z]/.test(password);
  const hasNumber = /[0-9]/.test(password);

  let isValid = true;

  if (password.length < minLength) {
    signUpError.classList.remove('hidden');
    passwordMinLengthError.classList.remove('hidden');
    msg.classList.add('show');

    isValid = false;
  }
  if (!hasUppercase || !hasLowercase) {
    signUpError.classList.remove('hidden');
    passwordUppercaseLowercaseError.classList.remove('hidden');
    msg.classList.add('show');

    isValid = false;
  }
  if (!hasNumber) {
    signUpError.classList.remove('hidden');
    passwordNumberRequiredError.classList.remove('hidden');
    msg.classList.add('show');

    isValid = false;
  }
  return isValid;
}

// Reset các thông báo lỗi
function resetErrorMsg() {
  signUpValidation.classList.add('hidden');
  signUpError.classList.add('hidden');
  emailError.classList.add('hidden');
  emailExist.classList.add('hidden');
  passwordMinLengthError.classList.add('hidden');
  passwordUppercaseLowercaseError.classList.add('hidden');
  passwordNumberRequiredError.classList.add('hidden');
  msg.classList.remove('show');
}

// Gắn sự kiện cho form
form.addEventListener('submit', (e) => {
  e.preventDefault();
  resetErrorMsg();
  const emailInput = email.value.trim();
  const usernameInput = username.value.trim();
  const passwordInput = password.value.trim();

  const empty = isEmpty(emailInput, usernameInput, passwordInput);
  const validationEmail = isValidationEmail(emailInput);
  const existEmail = isExistEmail(emailInput);
  const passwordValid = isPasswordValid(passwordInput);

  if (empty && validationEmail && existEmail && passwordValid) {
    // Create new user
    const user = {
      usercode: uniqueUsercode(),
      username: usernameInput,
      email: emailInput,
      password: passwordInput,
      role: 'user',
      status: 'Deactive',
      description: 'new user',
    };
    users.push(user);
    localStorage.setItem('userList', JSON.stringify(users)); // thêm vào localStorage
    signUpToast.classList.remove('hidden');
    msg.classList.add('show');

    //Chuyển trang Sign in, delay 2s
    setTimeout(() => {
      window.location.href = './sign-in.html';
    }, 800);
  }
});

// tạo Id unique
function uniqueUsercode() {
  const string = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
  const charRandom = string[Math.floor(Math.random() * string.length)];
  const number = Math.floor(Math.random() * 10);
  const uniqueUsercode = `${charRandom}${number}${number}`;

  if (users.some((u) => u.usercode === uniqueUsercode)) {
    uniqueUsercode();
  } else return uniqueUsercode;
}
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
email.addEventListener('input', resetErrorMsg);
username.addEventListener('input', resetErrorMsg);
password.addEventListener('input', resetErrorMsg);
