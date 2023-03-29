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


