import json


def verify(imageName, jsonString):
    jsonDict = json.loads(jsonString)

    if imageName == "IMG_0000.jpg":
        itemsList = ["DONUT MILKA 56G", "3.19", "DONUT OREO 73G", "3.19", "POP COLA 0.33L", "2.99", "SNICKERS BATON 75G", "2.99"]
        itemsString = toItemsString(itemsList)
        if jsonDict["items"] == itemsString:
            print("ITEMS OK!")
        else:
            print("ITEMS NOT OK!")


def toItemsString(items):
    itemsString = ""
    for i in range(0, len(items) - 1):
        itemsString += f"{items[i]}:{items[i + 1]};"

    return itemsString
