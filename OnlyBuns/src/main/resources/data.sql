-- TRUNCATE TABLE
--     locations,
--     users,
--     posts,
--     chats,
--     likes,
--     comments,
--     chat_members,
--     messages,
--     user_followers,
--     user_weekly_statistic
-- RESTART IDENTITY CASCADE;

INSERT INTO locations (id, latitude, longitude) VALUES
(1, 44.787197, 20.457273), 
(2, 45.267136, 19.833549), 
(3, 43.320904, 21.895410); 


INSERT INTO users (id, username, email, password, role, first_name, last_name, address, is_active, last_login_time) VALUES
(101, 'admin', 'admin@onlybuns.com', '$2a$10$NotRealHashForAdmin123OnlyBuns', 'ADMIN', 'John', 'Doe', 'Glavna 1, Beograd', true, '2025-09-08 15:30:00'),
(102, 'pera', 'pera.peric@onlybuns.com', '$2a$10$NotRealHashForPera456OnlyBuns', 'USER', 'Pera', 'Perić', 'Sporedna 10, Novi Sad', true, '2025-09-08 15:00:00'),
(103, 'mika', 'mika.mikic@onlybuns.com', '$2a$10$NotRealHashForMika789OnlyBuns', 'USER', 'Mika', 'Mikić', 'Bulevar 5, Niš', true, '2025-09-07 18:00:00'),
(104, 'deactivated', 'deactivated@onlybuns.com', '$2a$10$NotRealHashForInactiveOnlyBuns', 'USER', 'Jane', 'Smith', 'Ulica Borova 22, Kragujevac', false, '2025-09-05 09:00:00');

INSERT INTO posts (id, user_id, description, image_url, latitude, longitude, created_at) VALUES
(201, 102, 'Pogledajte mog zeku Duška! Uslikan u Novom Sadu.', 'test1', 45.267136, 19.833549, '2025-09-08 10:00:00'),
(202, 103, 'Moj ljubimac Gricko, najslađi zec u Nišu.', 'test2', 43.320904, 21.895410, '2025-09-08 11:30:00'),
(203, 101, 'Dobrodošli na OnlyBuns! Podelite slike vaših ljubimaca. Pozdrav iz Beograda!', 'test3', 44.787197, 20.457273, '2025-09-07 09:00:00'),
(204, 102, NULL, 'test4', 44.787197, 20.457273, '2025-09-09 14:00:00');

INSERT INTO likes (id, user_id, post_id, created_at) VALUES
(401, 101, 201, '2025-09-08 10:05:00'),
(402, 103, 201, '2025-09-08 10:06:00'),
(403, 102, 202, '2025-09-08 11:32:00'),
(404, 103, 204, '2025-09-09 14:05:00'),
(405, 101, 202, '2025-09-09 15:00:00'),
(406, 102, 203, '2025-09-09 16:00:00');

INSERT INTO comments (id, user_id, post_id, content, created_at) VALUES
(501, 103, 201, 'Dobrodošao Pera! Super post, Duško je presladak!', '2025-09-08 10:15:00'),
(502, 101, 201, 'Slažem se, odličan početak!', '2025-09-08 10:18:00'),
(503, 102, 203, 'Hvala admine!', '2025-09-07 09:30:00');

INSERT INTO chats (id, name, description, admin_id, created_at, is_active) VALUES
(301, 'Opšta Diskusija', 'Kanal za sve članove da diskutuju o raznim temama.', 101, '2025-09-08 12:00:00', true),
(302, 'Development Kanal', 'Razgovori o programiranju, Spring Boot-u i bazama.', 101, '2025-09-08 12:05:00', true),
(303, 'Ljubitelji zečeva - Novi Sad', 'Grupa za sve vlasnike zečeva iz Novog Sada i okoline.', 102, '2025-09-09 10:00:00', true),
(304, 'Arhivirani Kanal', 'Ovaj kanal više nije aktivan.', 101, '2025-01-01 12:00:00', false),
(305, 'Sumnjiva Rabota Zec', 'Kupoprodaja zečeva... sa karakterom.', 102, '2025-09-10 09:00:00', true);

INSERT INTO chat_members (id, user_id, chat_id, joined_at, role, is_active) VALUES
(601, 101, 301, '2025-09-08 12:00:00', 'ADMIN', true),
(602, 102, 301, '2025-09-08 12:01:00', 'MEMBER', true),
(603, 103, 301, '2025-09-08 12:02:00', 'MEMBER', true),
(604, 101, 302, '2025-09-08 12:05:00', 'ADMIN', true),
(605, 102, 302, '2025-09-08 12:06:00', 'MEMBER', true),
(606, 102, 303, '2025-09-09 10:00:00', 'ADMIN', true),
(607, 103, 303, '2025-09-09 10:05:00', 'MEMBER', true),
(608, 102, 305, '2025-09-10 09:00:00', 'ADMIN', true),
(609, 103, 305, '2025-09-10 09:01:00', 'MEMBER', true);

INSERT INTO messages (id, content, "timestamp", type, chat_id, sender_id) VALUES
(701, 'Zdravo svima!', '2025-09-08 12:03:00', 'TEXT', 301, 102),
(702, 'Ćao Pera, dobrodošao!', '2025-09-08 12:04:00', 'TEXT', 301, 103),
(703, 'Da li je neko imao iskustva sa Kafka streamovima?', '2025-09-08 13:00:00', 'TEXT', 302, 101),
(704, 'Jesam ja malo, šta te konkretno zanima?', '2025-09-08 13:05:00', 'TEXT', 302, 102),
(705, 'Dobrodošli svi u grupu! Ja sam Pera, admin.', '2025-09-09 10:01:00', 'TEXT', 303, 102),
(706, 'Pozdrav Pera, čujem da si ekspert. Imam na prodaju jednog zeca, A klasa, skoro nov, malo prešao.', '2025-09-10 09:05:00', 'TEXT', 305, 103),
(707, 'O, pa zdravo Mika. A klasa, kažeš? Daj malo više detalja. Koje je boje, koliko je star?', '2025-09-10 09:06:00', 'TEXT', 305, 102),
(708, 'Šampionsko poreklo! Boja... unikatna bež sa apstraktnim detaljima. Star je taman koliko treba. Ima taj ''vintage'' izgled.', '2025-09-10 09:07:00', 'TEXT', 305, 103),
(709, 'Apstraktni detalji? Zvuči kao da je upao u blato. Jel ima neke mane?', '2025-09-10 09:08:00', 'TEXT', 305, 102),
(710, 'Mane? Ma kakvi! To su karakteristike. Recimo, ima aerodinamične uši - jedno je malo kraće, za manji otpor vazduha.', '2025-09-10 09:09:00', 'TEXT', 305, 103),
(711, 'I energetski je efikasan, preferira da se odmara... horizontalno.', '2025-09-10 09:09:30', 'TEXT', 305, 103),
(712, 'Čekaj, znači fali mu jedno uvo i stalno leži? Jel uopšte živ?', '2025-09-10 09:11:00', 'TEXT', 305, 102),
(713, 'Živ?! Pa naravno! Pun je života! Skače u krug, doduše samo ulevo, ali sa neverovatnim entuzijazmom. Zovemo ga Ciklon.', '2025-09-10 09:12:00', 'TEXT', 305, 103),
(714, 'Znači, ćopav, polugluv zec koji se vrti u krug. Nudim ti 200 dinara i kesicu smokija.', '2025-09-10 09:14:00', 'TEXT', 305, 102),
(715, 'VREĐAŠ ME! ...ali pristajem ako su smoki sa kikirikijem.', '2025-09-10 09:15:00', 'TEXT', 305, 103),
(716, 'Upozorenje: Transakcije koje uključuju ''aerodinamične'' zečeve se obavljaju na sopstvenu odgovornost.', '2025-09-10 09:20:00', 'SYSTEM', 305, 101);

INSERT INTO user_followers (follower_id, followee_id) VALUES
(102, 101),
(102, 103),
(103, 102), 
(101, 103);

INSERT INTO user_weekly_statistic (user_id, likes, posts, followers) VALUES
(101, 1, 1, 1),
(102, 3, 2, 1),  
(103, 2, 1, 2);