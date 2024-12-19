## Rest API
For most objects, related objects are only referenced by UUIDs or entity references which also contain a type.
The Pipeline entity owns most of the other entity types, which is why the pipeline CRUD can be used to create and manipulate most entities whithout having to create or manipulate them using their own interfaces.
### Entity References
Entity references can be located by their object name which has a suffix `Ref`. Referenced entities have to exist in the database, otherwise they will be ignored by operations create, update and delete. In contrast, entities not marked as referenced entities will be created by create and update if they do not exist.