let users = JSON.parse(localStorage.getItem('userList')) || [];
users.sort((a, b) => a.usercode.localeCompare(b.usercode)); // sắp xếp mảng data

const search = document.getElementById('search-box');
const searchBtn = document.querySelector('.fa-magnifying-glass');
const tableBody = document.getElementById('table-body');

const pagination = document.getElementById('pagination');
const paginationList = document.getElementById('pagination-list');

const pagesPerButton = 5;
let pageArr = splitIntoPages(users, pagesPerButton);

// phân chia số trang pagination
function splitIntoPages(arr, number) {
  let pageArr = [];
  for (let i = 0; i < arr.length; i += number) {
    pageArr.push(arr.slice(i, i + number));
  }
  return pageArr;
}

// Render bảng theo từng page. Gắn sự kiện với button
let currentPage = 1;
pagination.addEventListener('click', (e) => {
  // cần cho vào trong, để ngoài sẽ null vì chưa render list btn
  const pageBtns = document.querySelectorAll('.page');

  // hiển thị theo từng trang khi click
  if (e.target.classList.contains('page')) {
    e.stopPropagation();
    currentPage = +e.target.textContent;
    renderTable(pageArr[currentPage - 1]);
  }
  // --> arrow right
  if (e.target.classList.contains('arrow-right')) {
    if (currentPage < pageArr.length) {
      if (currentPage > 3 && currentPage < pageArr.length - 1) {
        const btnHide = document.querySelectorAll('.btn-hide');
        const ellipsisAfter = document.querySelector('.ellipsis-after');
        btnHide[currentPage - 4].classList.remove('hidden');
        if (currentPage === btnHide.length + 3) {
          ellipsisAfter.classList.add('hidden');
        }
      }
      currentPage++;
      renderTable(pageArr[currentPage - 1]);
    } else {
      renderTable(pageArr[pageArr.length - 1]);
    }
  }
  // <-- arrow left
  if (e.target.classList.contains('arrow-left')) {
    if (currentPage > 1) {
      if (currentPage > 5 && currentPage <= pageArr.length) {
        const btnHide = document.querySelectorAll('.btn-hide');
        const ellipsisAfter = document.querySelector('.ellipsis-after');
        const ellipsisBefore = document.querySelector('.ellipsis-before');
        btnHide[currentPage - 6].classList.remove('hidden');
        if (currentPage === pageArr.length) {
          ellipsisAfter.classList.add('hidden');
          ellipsisBefore.classList.remove('hidden');
        }
        if (currentPage === 6) ellipsisBefore.classList.add('hidden');
      }
      currentPage--;
      renderTable(pageArr[currentPage - 1]);
    } else {
      renderTable(pageArr[0]);
    }
  }
  // nút ...
  if (e.target.classList.contains('ellipsis-after')) {
    const btnHide = document.querySelectorAll('.btn-hide');
    for (let i = 0; i < btnHide.length; i++) {
      btnHide[i].classList.remove('hidden');
    }
    e.target.classList.add('hidden');
  }
  // highlight btn
  pageBtns.forEach((b, index) => {
    index === currentPage - 1
      ? b.classList.add('highlight')
      : b.classList.remove('highlight');
  });
});

// Render danh sách button pagination
function renderListPagination(arr) {
  if (arr.length < 1) return;
  if (arr.length > 5) {
    paginationList.innerHTML = `<li class="pagination-element page highlight">1</li>`;
    for (let i = 2; i <= 4; i++) {
      paginationList.innerHTML += `<li class="pagination-element page">${i}</li>`;
    }
    paginationList.innerHTML += `<li class="pagination-element ellipsis-before hidden">...</li>`;
    for (let i = 5; i <= arr.length - 1; i++) {
      paginationList.innerHTML += `<li class="pagination-element page btn-hide hidden">${i}</li>`;
    }
    paginationList.innerHTML += `<li class="pagination-element ellipsis-after">...</li>
    <li class="pagination-element page">${arr.length}</li>
  `;
    return;
  }
  paginationList.innerHTML = `<li class="pagination-element page highlight">1</li>`;
  for (let i = 2; i < arr.length; i++) {
    paginationList.innerHTML += `<li class="pagination-element page">${i}</li>`;
  }
}

// Render bảng user
function renderTable(arr) {
  tableBody.innerHTML = '';
  tableBody.innerHTML = arr
    .map(
      (u) => `
    <tr id="${u.usercode}">
        <td>${u.usercode}</td>
        <td>${u.username}</td>
        <td>${u.email}</td>
        <td class="uppercase">${u.role}</td>
        <td>${dateConvert(u.birthday)}</td>
        <td>
            <span class="status-cell ${
              u.status === 'Active' ? 'active' : 'deactive'
            }">
            <div class="dot"></div>${u.status}</span>
        </td>
        <td>
            <i class="fa-solid fa-trash"></i>
            <i class="fa-solid fa-pen"></i>
        </td>
    </tr>
    `
    )
    .join('');
}

// Gắn sự kiện cho table
tableBody.addEventListener('click', (e) => {
  const tr = e.target.closest('tr');
  if (!tr) return;
  const userId = tr.getAttribute('id');
  const user = users.find((u) => u.usercode === userId);
  if (!user) return;
  // Sự kiện delete: tạo array users mới và lưu vào localStorage
  if (e.target.classList.contains('fa-trash')) {
    confirm('Bạn chắc chắn muốn xóa?');
    users = users.filter((u) => u.usercode !== user.usercode);
    pageArr = splitIntoPages(users, pagesPerButton);

    localStorage.setItem('userList', JSON.stringify(users));
    renderTable(pageArr[0]);
  } else if (e.target.classList.contains('fa-pen')) {
    // Sự kiện edit: lưu user vào sessionStorage để vào trang edit-user lấy ra sửa
    sessionStorage.setItem('userEdit', JSON.stringify(user));

    //Chuyển trang edit-user
    window.location.href = './edit-user.html';
  }
});

// Tìm kiếm username theo button
searchBtn.addEventListener('click', () => {
  const searchInput = search.value.trim();
  if (searchInput === '') return;
  const findUser = users.filter(
    (u) => u.username.toLowerCase() === searchInput.toLowerCase()
  );

  pageArr = splitIntoPages(findUser, pagesPerButton);
  renderListPagination(pageArr);
  renderTable(pageArr[0]);
  currentPage = 1;
});
// Tìm kiếm username tự động
search.addEventListener('input', () => {
  const searchInput = search.value.trim();
  if (searchInput === '') {
    return;
  }
  const findUser = users.filter((u) =>
    u.username.toLowerCase().includes(searchInput.toLowerCase())
  );

  pageArr = splitIntoPages(findUser, pagesPerButton);
  renderListPagination(pageArr);
  renderTable(pageArr[0]);
  currentPage = 1;
});
// Định dạng datetime
function dateConvert(dateStr) {
  if (typeof dateStr !== 'string') return '';
  const dateArr = dateStr.split('-');
  if (dateArr.length !== 3) return '';
  const year = dateArr[0];
  const month = dateArr[1];
  const day = dateArr[2];
  return `${day}/${month}/${year}`;
}
// In ra màn hình mặc định
renderTable(pageArr[0]);
renderListPagination(pageArr);
