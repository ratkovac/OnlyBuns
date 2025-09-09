OnlyBuns
Authorization
Ukoliko hocete da dozvolite samo userima sa odredjenom rolom da pristupe nekoj ruti, mozete koristiti middleware za autorizaciju. Na primer, ako zelite da dozvolite samo korisnicima sa rolom "admin" da pristupe ruti, mozete uraditi sledece: 
Iznad url-a rute dodajte     @PreAuthorize("hasAuthority('ROLE_ADMIN')") ili     @PreAuthorize("hasAuthority('ROLE_USER')") u zavisnosti od toga koju rolu zelite da dozvolite.

