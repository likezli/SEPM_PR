CREATE TABLE IF NOT EXISTS Article (
  ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  price DOUBLE NOT NULL,
  producer VARCHAR(50),
  sold INTEGER,
  picture VARCHAR(50),
  deleted BOOLEAN
);

CREATE TABLE IF NOT EXISTS Invoice (
  ID      INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  date    DATE NOT NULL,
  sum     DOUBLE NOT NULL,
  articlesCount INTEGER NOT NULL,
  payment VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS DetailInvoice (
  IID INTEGER REFERENCES Invoice(ID),
  AID INTEGER REFERENCES Article(ID),
  name VARCHAR(50) NOT NULL,
  purchasePrice DOUBLE NOT NULL,
  quantity INTEGER NOT NULL
);

INSERT INTO Article(name, price, producer, sold, deleted, picture) VALUES
  ('Riding Helmet Black', 29.90, 'HorsePower', 13, FALSE, 'res/img/HelmetBlack.jpg' ),
  ('Riding Helmet Brown', 29.90, 'Horsepower', 10, FALSE, 'res/img/HelmetBrown.jpg'),
  ('Riding Helmet White', 29.90, 'Horsepower', 6, FALSE, 'res/img/HelmetWhite.jpg'),
  ('Riding Boots Red', 30.90, 'LuckyHorse', 5, FALSE, 'res/img/BootsRed.jpg'),
  ('Riding Boots Grey', 22.90, 'LuckyHorse', 7, FALSE, 'res/img/BootsGrey.jpg'),
  ('Riding Boots Leather Brown', 54.90, 'LuckyHorse', 8, FALSE,'res/img/BootsBrown.jpg'),
  ('Riding Boots Leather Black', 54.90, 'LuckyHorse', 12, FALSE, 'res/img/BootsBlack.jpg'),
  ('Horse Blanket Red', 59.90, 'HorseLovers', 5, FALSE, 'res/img/BlanketRed.jpg'),
  ('Horse Blanket Black', 65.90, 'HorseLovers', 8, FALSE, 'res/img/BlanketBlack.jpg'),
  ('Horse Blanket Blue', 59.90, 'HorseLovers', 9, TRUE, 'res/img/BlanketBlue.jpg'),
  ('Horse Blanket Green', 65.90, 'HorseLovers', 4, FALSE, 'res/img/BlanketGreen.jpg'),
  ('Saddle Leather Brown', 299.00, 'HorseSaddles', 3, FALSE, 'res/img/SaddleBrown.jpg'),
  ('Saddle Leather Black', 299.00, 'HorseSaddles', 3, FALSE, 'res/img/SaddleBlack.jpg'),
  ('Saddle Leather Grey', 259.00, 'HorseSaddles', 5, FALSE, ''),
  ('Saddle Leather White', 259.00, 'HorseSaddles', 5, FALSE, 'res/img/SaddleWhite.jpg'),
  ('Horse Feed Basic', 19.90, 'HorseProtein', 46, FALSE, ''),
  ('Horse Feed Classic', 29.90, 'HorseProtein', 65, FALSE, ''),
  ('Horse Feed Gold', 35.90, 'HorseProtein', 60, FALSE, ''),
  ('Horse Feed Platinum', 39.90, 'HorseProtein', 35, FALSE, '');

INSERT INTO Invoice(date, sum, articlesCount, payment) VALUES
  (parsedatetime('2016-03-30', 'yyyy-MM-dd'), 3578.90, 8, 'Credit Card'),
  (parsedatetime('2016-03-30', 'yyyy-MM-dd'), 7908.40, 9, 'Credit Card'),
  (parsedatetime('2016-04-02', 'yyyy-MM-dd'), 1311.10 , 5, 'Cash'),
  (parsedatetime('2016-04-03', 'yyyy-MM-dd'), 658.80 , 2, 'Cash'),
  (parsedatetime('2016-04-06', 'yyyy-MM-dd'), 1236.20 , 3, 'Credit Card'),
  (parsedatetime('2016-04-09', 'yyyy-MM-dd'), 209.30 , 1, 'Cash');

INSERT INTO DetailInvoice(IID, AID, name, purchasePrice, quantity) VALUES
  (1, 1, 'Riding Helmet Black', 29.90, 5),
  (1, 2, 'Riding Helmet Brown', 29.90, 3),
  (1, 6, 'Riding Boots Leather Brown', 54.90, 3),
  (1, 7, 'Riding Boots Leather Black', 54.90, 5),
  (1, 12, 'Saddle Leather Brown', 299.00, 3),
  (1, 13, 'Saddle Leather Black', 299.00, 3),
  (1, 18, 'Horse Feed Gold', 35.90, 10),
  (1, 17, 'Horse Feed Classic', 29.90, 25),
  (2, 8, 'Horse Blanket Red', 59.90, 5),
  (2, 9, 'Horse Blanket Black', 65.90, 8),
  (2, 10, 'Horse Blanket Blue', 59.90, 9),
  (2, 11, 'Horse Blanket Green', 65.90, 4),
  (2, 14, 'Saddle Leather Grey', 259.00, 5),
  (2, 15, 'Saddle Leather White', 259.00, 5),
  (2, 16, 'Horse Feed Basic', 19.90, 35),
  (2, 19, 'Horse Feed Platinum', 39.90, 25),
  (2, 18, 'Horse Feed Gold', 35.90, 50),
  (3, 4, 'Riding Boots Red', 30.90, 5),
  (3, 5, 'Riding Boots Grey', 22.90, 7),
  (3, 3, 'Riding Helmet White', 29.90, 6),
  (3, 17, 'Horse Feed Classic', 29.90, 20),
  (3, 16, 'Horse Feed Basic', 19.90, 11),
  (4, 6, 'Riding Boots Leather Brown', 54.90, 5),
  (4, 7, 'Riding Boots Leather Black', 54.90, 7),
  (5, 17, 'Horse Feed Classic', 29.90, 20),
  (5, 19, 'Horse Feed Platinum', 39.90, 10),
  (5, 1, 'Riding Helmet Black', 29.90, 8),
  (6, 2, 'Riding Helmet Brown', 29.90, 7);


