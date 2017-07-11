InventorySortPlugin

# InventorySort!

I was disappointed to find the only good inventory sort plugins that were available are premium. So I decided to make my own!

​
## Features:
Sort your inventory! Simply type the command (more below) or click the middle mouse button!
Sort your chests too!


## Default Commands:
`/sort` - The main command that will sort a player inventory or the inventory of a chest a player is looking at.
`/s` - Shorthand for `/sort`
`/sort-toggle [playername]` - Command able to toggle sorting chests by hitting them (left clicking), with playername argument as an optional parameter if you have permissions.
`/st` - Shorthand of /sort-toggle


## Usage:
You can use `/sort` or `/s` to sort your inventory or the inventory of the chest you are looking at.
Alternatively, you can left-click chests to sort them (if sort-toggle is enabled), or middle click in the inventory screen to sort
The default sort will toggle between ascending and descending order by item ID. Adding a type after the sort command will force that type of sort:

Type: a, asc, ascend, ascending, d, des, descend, descending
Example: `/sort d` - Sort inventory in descending order


## Default config.yml:
Code (Text):

```
messages:
  inv-message: '&8Sorted your inventory'
  inv-message-send: false
  chest-message: '&8Sorted your chest'
  chest-message-send: false
  toggle-message-on: '&8Chest sorting disabled'
  toggle-message-off: '&8Chest sorting enabled'
  toggle-message-send: false
  sort-type-message-int: '&8Sorting by ID'
  sort-type-message-string: '&8Sorting by Name'
  secure-message: '&8Chest secured, only you can sort this chest'
  secure-message-duplicate: '&cChest is already secured'
  secure-message-error: '&cNot a chest, try again'
  secure-message-owned: '&cChest is secured and cannot be sorted by you'
  secure-message-send: true
cooldown:
  cooldown-hot-message: '&cYou still have time left before you can use the command'
  cooldown-time: 0
permissions:
  player-lackof-permission: '&cYou don''t have permission to sort your inventory'
  chest-lackof-permission: '&cYou don''t have permission to sort chests'
  toggle-lackof-permission: '&cYou don''t have permission to toggle chest sorting'
  toggle-others-lackof-permission: '&cYou don''t have permission to toggle other players
    chest sorting'
  type-lackof-permission: '&cYou don''t have permission to change sorting behavior'
  secure-lackof-permission: '&cYou don''t have permission to secure a chest'
sort-options:
  sort-command: sort
  sort-command-alias: s
  sort-help: Sorts inventory of player or of a chest.
  sort-usage: "/<command> [type] \n§6Type:§f (a, asc, d, des) or (up, abc, down, xyz)\
    \ \n§6Example:§f /<command> descending - Sort inventory in descending order"
toggle-options:
  toggle-command: sort-toggle
  toggle-command-alias: st
  toggle-help: Toggles the function of hitting chests to sort them.
  toggle-usage: "/<command> [target] \n§6Target:§f playername \n§6Example:§f /<command>\
    \ - Toggles ability to sort chests on click \n§6Example:§f /<command> Notch -\
    \ Toggles ability to sort chests for Notch"
type-options:
  type-command: sort-type
  type-command-alias: sy
  type-help: Changes the default sorting behavior.
  type-usage: "/<command> [type] \n§6Type:§f id, name \n§6Example:§f /<command> -\
    \ Toggles whether sorting should be by \nID or by names\n§6Example:§f /<command>\
    \ id - Forces type to be by ID\n§6Example:§f /<command> name - Forces type to\
    \ be by names"
secure-options:
  secure-command: sort-secure
  secure-command-alias: sc
  secure-help: Secures chest so only you can sort this chest
  secure-usage: "/<command> \n§6Example:§f /<command> - Secures selected chest"
debug:
  toggle-player-offline: '&cPlayer is not online'
  monster-egg-error: '&cMonster eggs can''t be stacked'
```
 

## Permissions:
- inventorysort.sort - Allows you to sort your inventory
- inventorysort.sortchest - Allows you to sort a chest, with inventorysort.sort as a child permission
- inventorysort.sorttoggle - Allows you to toggle the sorting command
- inventorysort.sorttoggle.others - Allows you to toggle other players sorting command, with inventorysort.sorttoggle as a child permission
- inventorysort.sorttype - Changes the type of sorting to take place.
- inventorysort.* - Allows you to do all available sort commands, contains all other permissions as children


## Current Features:
- Permissions (Added 1.1.0)
- Configurations (Added 1.2.0)
- Cooldown (Added 1.3.0)
- Toggling (Added 1.3.0)
- Multi-Language (Added 1.4.0)
- Security (Added 1.5.0)
- More ways to sort (alphabetical) (Added 1.5.0)

**NOTE** I (George) will no longer be working on the project.