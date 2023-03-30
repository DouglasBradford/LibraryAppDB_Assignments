select * from users;

-- US 01
select count(id) from users;
-- 1855

select count(distinct id) from users;
-- 1855

-- RESULT --> MANUALLY IT IS PASSED

-- US 02
select count(*) from book_borrow
where is_returned = 0; -- 474

-- US 03
select name from book_categories;

-- US 04
select b.name,author,isbn, year,bc.name,b.description
from books b join book_categories bc on b.book_category_id = bc.id
where b.name = 'Cosmos';

-- US 05
select bc.name,count(*) from book_borrow bb join books b on bb.book_id= b.id
         join book_categories bc on b.book_category_id = bc.id
where is_returned = 0
group by bc.name
order by count(*) desc;

-- US 06
select b.name,isbn,year,author,bc.name from books b join book_categories bc on b.book_category_id = bc.id
where b.name ='Cosmos';

select b.name,isbn,year,author,bc.name from books b join book_categories bc on b.book_category_id = bc.id
order by isbn desc;

-- US 07
#borrowed book id
select book_id from book_borrow bb join books b on bb.book_id=b.id
where is_returned = 0 and user_id = 5759; #borrowed book id - 9633 unreturned books from user 5759

select b.name from book_borrow bb join books b on bb.book_id=b.id
where is_returned = 0 and user_id = (select id from users where email like 'student7@library');
#user id
select id from users where email like 'student7@library'; #user id  - 5759
#book name
select name from books where id = 9633;
#combined
select name from books where id = (select book_id from book_borrow
where is_returned = 0 and user_id = (select id from users where email like 'student7@library'));




