create database JukeBox;
use  jukebox;


create table customer(customerId int primary key auto_increment, customerName varchar(50) not null,

customerEmail varchar(50) not null unique);


create table login(customerEmail varchar(50) not null unique, CustomerPassword varchar(30) not null,

constraint customerEmailRef foreign key(customerEmail) references customer(customerEmail));

alter table login drop constraint customerEmailRef; 
alter table login add constraint customerEmailRef  foreign key(customerEmail) references customer(customerEmail) on delete cascade; 


create table songDetails(SongDetailsId int auto_increment primary key, SongAlbum varchar(100) , SongAuthor varchar(50) not null);

drop table songDetails;


create table songs(SongId int primary key auto_increment, SongGenre varchar(30), SongName varchar(100),

songDetailsId int not null, songFile varchar(200) not null,

constraint referSongDetails foreign key(songDetailsId) references songDetails(songDetailsId));

drop table songs;


create table podcasts(podcastId int primary key auto_increment, podcastName varchar(100) not null, celebrity varchar(50) not null);



create table episodes(EpisodeId int primary key auto_increment, EpisodeName varchar(100) not null, EpisodeDate date not null

, podcastId int not null, episodeFile varchar(200) not null,

constraint referPodcast foreign key(podcastId) references podcasts(podcastId));

alter table episodes add unique unique_episodes_in_podcast(episodeName, podcastId);

create table playlists(playListId int primary key auto_increment,  playListName varchar(100) not null default "NEW PLAYLIST" , customerId int not null,

constraint referCustomer foreign key(customerId) references customer(customerId) on delete cascade);

drop table playlists;


create table songsInPlaylist(playListId int not null, songId int not null,

constraint referPlaylistForSong foreign key(playlistId) references playlists(playlistId) on delete cascade,

constraint referSong foreign key(songId) references songs(songId) ,

constraint uniqueSongPlaylist primary key(playlistId, songId));

drop table songsInPlaylist;



create table podcastsInPlaylist(playlistId int not null, podcastId int not null,

constraint referPlaylistForPodcast foreign key(playlistId) references playlists(playlistId) on delete cascade,

constraint referPodcastForPlaylist foreign key(podcastId) references podcasts(podcastId),

constraint primary key(playlistId, podcastId));

drop table podcastsInPlaylist;



insert into songdetails (songAuthor) values("Alan Walker");

insert into songs (songGenre, SongName, songDetailsId, songFile) 
values("Electro House", "Spectre", 1,"Resources/Alan Walker - The Spectre 2017 (Pop Stars).wav");


insert into songDetails (songAuthor) values("Ed sheeran");

insert into songs (songGenre, SongName, songDetailsId, songFile)  
values("pop", "Shape of you", 2, "Resources/Ed Sheeran - Shape Of You.wav");

insert into songdetails (songAlbum, songAuthor) 
values("Paiyaa", "Haricharan & Tanvi Shah");

insert into songs (songGenre, SongName, songDetailsId, songFile)  
values("Feature Film Soundtrack", "Thuli Thuli", 3, "Resources/Haricharan & Tanvi Shah - Thuli Thuli Mazhaiyaai.wav");


insert into songdetails(SongAuthor) values("HipHop tamizha");

insert into songs (songGenre, SongName, songDetailsId, songFile)  
values("Hip-Hop/Rap", "Takkaru Takkaru", 4, "Resources/Hiphop Tamizha - Takkaru Takkaru.wav");

insert into songDetails(SongAuthor) values("Chainsmokers");

insert into songs (songGenre, SongName, songDetailsId, songFile)
values("Future Bass / pop", "Closer", 5, "Resources/The Chainsmokers - Closer.wav" );  

update songs set songFile = "Resources/The Chainsmokers - Closer.wav"
where songId = 5;


insert into podcasts(podcastName,celebrity) values("The Power of Vocal Dynamics", "Sheena Walker");

insert into episodes(episodeName, episodeDate, podcastId, episodeFile ) values("The power of charisma", '2017-11-06' ,1,
"Resources/podcasts/Power of vocal dynamics/podcast_-the-power-vocal-dynamics_the-power-charisma_1000394606916.wav" );

insert into episodes(episodeName, episodeDate, podcastId, episodeFile ) values("The power of presence and body language", '2017-11-05' ,1,
"Resources/podcasts/Power of vocal dynamics/podcast_-the-power-vocal-dynamics_the-power-presence-body_1000394606919.wav" );

insert into episodes(episodeName, episodeDate, podcastId, episodeFile ) values("The power of vocal dynamics", '2017-11-04' ,1,
"Resources/podcasts/Power of vocal dynamics/podcast_-the-power-vocal-dynamics_the-power-vocal-dynamics-_1000394606920.wav" );


insert into podcasts(podcastName,celebrity) values("Ponniyin Selvan", "P.Raghavi");
insert into episodes(episodeName, episodeDate, podcastId, episodeFile ) values("Introduction to kadhai kadhaiyai kadhaikalam", '2020-06-27' ,2,
"Resources/podcasts/Ponniyin selvan/https3a2f2fd3ctxlq1ktw2nlcloudfrontnet2fproduction2f2020-5-242f84887764-44100-2-1f68459a2aad3mp3.wav" );

update episodes set episodeFile = "Resources/podcasts/Ponniyin selvan/ponniyinSelvanIntro.wav" where episodeName = "Introduction to kadhai kadhaiyai kadhaikalam";

insert into episodes(episodeName, episodeDate, podcastId, episodeFile ) values("Chapter-1 Adi Thirunal", '2020-06-27' ,2,
"Resources/podcasts/Ponniyin selvan/https3a2f2fd3ctxlq1ktw2nlcloudfrontnet2fproduction2f2020-4-312f78287187-44100-2-42a079f0a482mp3.wav" );

update episodes set episodeFile = "Resources/podcasts/Ponniyin selvan/PonniyinSelvanEpisode1.wav" where episodeName = "Chapter-1 Adi Thirunal";




