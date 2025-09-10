DELETE FROM messages;
DELETE FROM chat_members;
DELETE FROM chats;
DELETE FROM comments;
DELETE FROM likes;
DELETE FROM posts;
DELETE FROM user_weekly_statistic;
DELETE FROM user_followers;

INSERT INTO users (id, username, email, password, role, first_name, last_name, address, is_active, last_login_time) VALUES
                                                                                                                        (1, 'admin', 'admin@onlybuns.com', '123', 'admin', 'John', 'Doe', 'Glavna 1, Beograd', true, '2025-09-08 15:30:00'),
                                                                                                                        (2, 'pera', 'pera.peric@onlybuns.com', '123', 'user', 'Pera', 'Perić', 'Sporedna 10, Novi Sad', true, '2025-08-08 15:00:00'),
                                                                                                                        (3, 'mika', 'mika.mikic@onlybuns.com', '123', 'user', 'Mika', 'Mikić', 'Bulevar 5, Niš', true, '2025-09-07 18:00:00'),
                                                                                                                        (4, 'deactivated', 'deactivated@onlybuns.com', '123', 'user', 'Jane', 'Smith', 'Ulica Borova 22, Kragujevac', false, '2025-09-05 09:00:00');

INSERT INTO posts (id, user_id, description, image_url, latitude, longitude, created_at) VALUES
                                                                                             (1, 2, 'Pogledajte mog zeku Duška! Uslikan u Novom Sadu.', 'http://localhost:8080/images/originals/pexels-cerenvisuals-33285252.jpg', 45.267136, 19.833549, '2025-09-08 10:00:00'),
                                                                                             (2, 3, 'Moj ljubimac Gricko, najslađi zec u Nišu.', 'http://localhost:8080/images/originals/pexels-cerenvisuals-33285252.jpg', 43.320904, 21.895410, '2025-09-08 11:30:00'),
                                                                                             (3, 1, 'Dobrodošli na OnlyBuns! Podelite slike vaših ljubimaca. Pozdrav iz Beograda!', 'http://localhost:8080/images/originals/pexels-shvetsa-4588065.jpg', 44.787197, 20.457273, '2025-09-07 09:00:00'),
                                                                                             (4, 2, NULL, 'http://localhost:8080/images/originals/pexels-cerenvisuals-33285252.jpg', 44.787197, 20.457273, '2025-09-09 14:00:00');

INSERT INTO likes (id, user_id, post_id, created_at) VALUES
                                                         (8, 1, 1, '2025-09-08 10:05:00'),
                                                         (7, 3, 1, '2025-09-08 10:06:00'),
                                                         (3, 2, 2, '2025-09-08 11:32:00'),
                                                         (4, 3, 4, '2025-09-09 14:05:00'),
                                                         (5, 1, 2, '2025-09-09 15:00:00'),
                                                         (6, 2, 3, '2025-09-09 16:00:00');

INSERT INTO comments (id, user_id, post_id, content, created_at) VALUES
                                                                     (1, 3, 1, 'Dobrodošao Pera! Super post, Duško je presladak!', '2025-09-08 10:15:00'),
                                                                     (2, 1, 1, 'Slažem se, odličan početak!', '2025-09-08 10:18:00'),
                                                                     (3, 2, 3, 'Hvala admine!', '2025-09-07 09:30:00');

INSERT INTO chats (id, name, description, admin_id, created_at, is_active) VALUES
                                                                               (1, 'Opšta Diskusija', 'Kanal za sve članove da diskutuju o raznim temama.', 1, '2025-09-08 12:00:00', true),
                                                                               (2, 'Development Kanal', 'Razgovori o programiranju, Spring Boot-u i bazama.', 1, '2025-09-08 12:05:00', true),
                                                                               (3, 'Ljubitelji zečeva - Novi Sad', 'Grupa za sve vlasnike zečeva iz Novog Sada i okoline.', 2, '2025-09-09 10:00:00', true),
                                                                               (4, 'Arhivirani Kanal', 'Ovaj kanal više nije aktivan.', 1, '2025-01-01 12:00:00', false),
                                                                               (5, 'Sumnjiva Rabota Zec', 'Kupoprodaja zečeva... sa karakterom.', 2, '2025-09-10 09:00:00', true);

INSERT INTO chat_members (id, user_id, chat_id, joined_at, role, is_active) VALUES
                                                                                (1, 1, 1, '2025-09-08 12:00:00', 'ADMIN', true),
                                                                                (2, 2, 1, '2025-09-08 12:01:00', 'MEMBER', true),
                                                                                (3, 3, 1, '2025-09-08 12:02:00', 'MEMBER', true),
                                                                                (4, 1, 2, '2025-09-08 12:05:00', 'ADMIN', true),
                                                                                (5, 2, 2, '2025-09-08 12:06:00', 'MEMBER', true),
                                                                                (6, 2, 3, '2025-09-09 10:00:00', 'ADMIN', true),
                                                                                (7, 3, 3, '2025-09-09 10:05:00', 'MEMBER', true),
                                                                                (8, 2, 5, '2025-09-10 09:00:00', 'ADMIN', true),
                                                                                (9, 3, 5, '2025-09-10 09:01:00', 'MEMBER', true);

INSERT INTO messages (id, content, "timestamp", type, chat_id, sender_id) VALUES
                                                                              (1, 'Zdravo svima!', '2025-09-08 12:03:00', 'TEXT', 1, 2),
                                                                              (2, 'Ćao Pera, dobrodošao!', '2025-09-08 12:04:00', 'TEXT', 1, 3),
                                                                              (3, 'Da li je neko imao iskustva sa Kafka streamovima?', '2025-09-08 13:00:00', 'TEXT', 2, 1),
                                                                              (4, 'Jesam ja malo, šta te konkretno zanima?', '2025-09-08 13:05:00', 'TEXT', 2, 2),
                                                                              (5, 'Dobrodošli svi u grupu! Ja sam Pera, admin.', '2025-09-09 10:01:00', 'TEXT', 3, 2),
                                                                              (6, 'Pozdrav Pera, čujem da si ekspert. Imam na prodaju jednog zeca, A klasa, skoro nov, malo prešao.', '2025-09-10 09:05:00', 'TEXT', 5, 3),
                                                                              (7, 'O, pa zdravo Mika. A klasa, kažeš? Daj malo više detalja. Koje je boje, koliko je star?', '2025-09-10 09:06:00', 'TEXT', 5, 2),
                                                                              (8, 'Šampionsko poreklo! Boja... unikatna bež sa apstraktnim detaljima. Star je taman koliko treba. Ima taj ''vintage'' izgled.', '2025-09-10 09:07:00', 'TEXT', 5, 3),
                                                                              (9, 'Apstraktni detalji? Zvuči kao da je upao u blato. Jel ima neke mane?', '2025-09-10 09:08:00', 'TEXT', 5, 2),
                                                                              (10, 'Mane? Ma kakvi! To su karakteristike. Recimo, ima aerodinamične uši - jedno je malo kraće, za manji otpor vazduha.', '2025-09-10 09:09:00', 'TEXT', 5, 3),
                                                                              (11, 'I energetski je efikasan, preferira da se odmara... horizontalno.', '2025-09-10 09:09:30', 'TEXT', 5, 3),
                                                                              (12, 'Čekaj, znači fali mu jedno uvo i stalno leži? Jel uopšte živ?', '2025-09-10 09:11:00', 'TEXT', 5, 2),
                                                                              (13, 'Živ?! Pa naravno! Pun je života! Skače u krug, doduše samo ulevo, ali sa neverovatnim entuzijazmom. Zovemo ga Ciklon.', '2025-09-10 09:12:00', 'TEXT', 5, 3),
                                                                              (14, 'Znači, ćopav, polugluv zec koji se vrti u krug. Nudim ti 200 dinara i kesicu smokija.', '2025-09-10 09:14:00', 'TEXT', 5, 2),
                                                                              (15, 'VREĐAŠ ME! ...ali pristajem ako su smoki sa kikirikijem.', '2025-09-10 09:15:00', 'TEXT', 5, 3),
                                                                              (16, 'Upozorenje: Transakcije koje uključuju ''aerodinamične'' zečeve se obavljaju na sopstvenu odgovornost.', '2025-09-10 09:20:00', 'SYSTEM', 5, 1);

INSERT INTO user_followers (follower_id, followee_id) VALUES
                                                          (2, 1),
                                                          (2, 3),
                                                          (3, 2),
                                                          (1, 3);

INSERT INTO user_weekly_statistic (user_id, likes, posts, followers) VALUES
                                                                         (1, 1, 1, 1),
                                                                         (2, 3, 2, 1),
                                                                         (3, 2, 1, 2);