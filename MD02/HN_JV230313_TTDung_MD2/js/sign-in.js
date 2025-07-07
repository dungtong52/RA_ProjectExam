const users = JSON.parse(localStorage.getItem('userList')) || [];

const form = document.getElementById('sign-in-form');
const email = document.getElementById('email');
const password = document.getElementById('password');
const saveLogin = document.getElementById('save-loggin-checkbox');

const msg = document.getElementById('msg');
const loginValidation = document.getElementById('login-validation');
const loginToast = document.getElementById('login-toast');
const loginError = document.getElementById('login-error');

// Kiểm tra email, password rỗng
function isEmpty(email, password) {
  if (email === '' || password === '') {
    loginValidation.classList.remove('hidden');
    msg.classList.add('show');
  }
  return email !== '' && password !== '';
}

// Kiểm tra tồn tại email và password
function isExistEmailAndPassword(email, password) {
  let isExist = true;
  const user = users.find((u) => u.email === email);
  if (!user || user.password !== password) {
    msg.classList.add('show');
    loginError.classList.remove('hidden');
    isExist = false;
  }
  return isExist;
}
// Lưu người đăng nhập vào localStorage
function saveCurrentUser(email) {
  const user = users.find((u) => u.email === email);
  localStorage.setItem('currentUser', JSON.stringify(user));
}

// Reset các thông báo lỗi
function resetErrorMsg() {
  loginValidation.classList.add('hidden');
  loginToast.classList.add('hidden');
  loginError.classList.add('hidden');
  msg.classList.remove('show');
}

// Gắn sự kiện cho form
form.addEventListener('submit', (e) => {
  e.preventDefault();
  resetErrorMsg();
  const emailInput = email.value.trim();
  const passwordInput = password.value.trim();

  const empty = isEmpty(emailInput, passwordInput);
  const existEmailAndPassword = isExistEmailAndPassword(
    emailInput,
    passwordInput
  );

  if (empty && existEmailAndPassword) {
    loginToast.classList.remove('hidden');
    msg.classList.add('show');

    // Tạo object người dùng vừa đăng nhập có thuộc tính thời gian đăng nhập
    // save user vừa đăng nhập thành loggedInUser trong localStorage
    if (saveLogin.checked) {
      const loggedInUser = {
        email: emailInput,
        password: passwordInput,
        loginTime: Date.now(),
      };
      localStorage.setItem('loggedInUser', JSON.stringify(loggedInUser));
    } else localStorage.removeItem('loggedInUser');

    saveCurrentUser(emailInput);

    //Chuyển trang Dashboard, delay 2s
    setTimeout(() => {
      window.location.href = './dashboard.html';
    }, 800);
  }
});
// Chức năng ghi nhớ thông tin đăng nhập
window.addEventListener('load', () => {
  const loggedIn = JSON.parse(localStorage.getItem('loggedInUser'));
  if (loggedIn) {
    const loginExpired = 24 * 60 * 60 * 1000;

    // chưa hết 1 ngày thì vẫn còn hạn lưu trữ dữ liệu nhập
    if (Date.now() - loginExpired < loggedIn.loginTime) {
      email.value = loggedIn.email;
      password.value = loggedIn.password;
    } else localStorage.removeItem('loggedInUser');
  }
});

// Reset thông báo khi nhập vào username và input
username.addEventListener('input', resetErrorMsg);
password.addEventListener('input', resetErrorMsg);
