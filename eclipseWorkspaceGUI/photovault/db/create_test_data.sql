---
-- Create folders for PhtoFolder test
---
 
delete from photo_collections;
insert into photo_collections (collection_id, parent, collection_name, collection_desc, create_time, last_modified) values (1, null, 'Top', '', NULL, null);
insert into photo_collections (collection_id, parent, collection_name, collection_desc, create_time, last_modified)  values ( 100, 1, 'subfolderTest', '', NULL, null);
insert into photo_collections  (collection_id, parent, collection_name, collection_desc, create_time, last_modified)  values ( 101, 100, 'Subfolder1', '', NULL, null);
insert into photo_collections (collection_id, parent, collection_name, collection_desc, create_time, last_modified)  values ( 102, 100, 'Subfolder2', '', NULL, null);
insert into photo_collections (collection_id, parent, collection_name, collection_desc, create_time, last_modified)  values ( 103, 100, 'Subfolder3', '', NULL, null);
insert into photo_collections (collection_id, parent, collection_name, collection_desc, create_time, last_modified)  values ( 104, 100, 'Subfolder4', '', NULL, null);
insert into photo_collections (collection_id, parent, collection_name, collection_desc, create_time, last_modified)  values ( 105, 1, 'testPhotoRetrieval', '', NULL, null);

delete from photos;
insert into photos(photo_id, description) values( 1, 'testPhoto1' );
insert into photos(photo_id, description) values( 2, 'testPhotoRetrieval1' );
insert into photos(photo_id, description) values( 3, 'testPhotoRetrieval2' );

delete from collection_photos;
insert into collection_photos (collection_id, photo_id) values( 105, 2 );
insert into collection_photos (collection_id, photo_id) values( 105, 3 );
