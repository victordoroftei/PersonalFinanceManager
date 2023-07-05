import copy
import re

from classes.receipt import Receipt


class Postprocessor:
    def __init__(self, filePath, imagePath):
        self.__filePath = filePath
        self.__imagePath = imagePath
        self.__lines = []
        self.__receipt = None

        self.readLinesFromFile()
        self.__loadPossibleRetailerDict()

    def readLinesFromFile(self):
        file = open(self.__filePath, "r")

        line = file.readline()
        self.__lines = []
        while line:
            self.__lines.append(line.strip("\n"))
            line = file.readline()

        # print(self.__lines)

        file.close()

    def applyItemRegex(self):
        items = list()
        total = 0.0

        retailer = None
        for possibleRetailerKeyword in self.__possibleRetailerDict.keys():
            if possibleRetailerKeyword.upper() in self.__lines[0].upper():
                retailer = self.__possibleRetailerDict[possibleRetailerKeyword]
                break

        if retailer is None:
            for possibleRetailerKeyword in self.__possibleRetailerDict.keys():
                if possibleRetailerKeyword.upper() in self.__lines[1].upper():
                    retailer = self.__possibleRetailerDict[possibleRetailerKeyword]
                    break

        for line in self.__lines:
            result = re.search(r"(.*)(\s+[0-9]+[,\.][0-9]{2}\-?\s*[ABCD])", line)
            if result is not None and result.groups() is not None:
                price = result.group(2).strip(" ABCD").replace("-", "").replace(",", ".")
                try:
                    price = float(price)
                except Exception:
                    price = None

                if "DISCOUNT" in result.group(1).upper():
                    items.append(("DISCOUNT", (-1) * price))
                    total -= price

                else:
                    items.append((result.group(1), price))
                    total += price

                # print(result.groups())

        total = round(total, 2)
        # print(items)
        # print(f"Total: {total}")

        self.__receipt = Receipt(self.__filePath, self.__imagePath, items, total, retailer)

    def applyTotalRegex(self):
        detectedTotalAttempt = 0.0
        detectedTotals = []
        detectedTotal = 0.0
        chosenDetectedTotal = False

        for line in self.__lines:
            if "TOTAL" in line.upper() and "TVA" not in line.upper():
                result = re.search(r"[0-9]+[\.,][0-9]{2}", line)
                if result is not None:
                    detectedTotalAttempt = result.group(0).strip().replace(",", ".")
                    # print(detectedTotalAttempt, "******")
                    try:
                        detectedTotal = float(detectedTotalAttempt)
                        detectedTotals.append(detectedTotal)
                    except Exception:
                        detectedTotal = None

        for dt in detectedTotals:
            if dt == self.__receipt.calculatedTotal:
                detectedTotal = dt
                chosenDetectedTotal = True

        leastDifference = self.__receipt.calculatedTotal
        if not chosenDetectedTotal:
            for dt in detectedTotals:
                currentDifference = abs(dt - self.__receipt.calculatedTotal)
                if currentDifference < leastDifference:
                    leastDifference = currentDifference
                    detectedTotal = dt

        # print(f"Detected Totals: {detectedTotals}")
        # print(f"Detected Total: {detectedTotal}")
        self.__receipt.detectedTotal = detectedTotal

    def applyDateAndTimeRegex(self):
        dateString = ""
        timeString = ""

        for line in reversed(self.__lines):
            result = re.search(r"[DB]A[T7]A\s*([0-9]{2}\/[0-9]{2}\/[0-9]{4})\s*[0OG][PRK]A\-*\s*([0-9]{2}\-[0-9]{2}\-[0-9]{2})", line)
            if result is not None and result.groups() is not None:
                dateString = result.group(1)
                # print(f"Date String: {dateString}")

                timeString = result.group(2)
                # print(f"Time String: {timeString}")

        formattedString = self.__formatDate(dateString, timeString)
        self.__receipt.receiptDate = formattedString

    def printJson(self):
        print(self.__receipt.toJson())

    @property
    def bon(self):
        return self.__receipt

    def __loadPossibleRetailerDict(self):
        self.__possibleRetailerDict = {
            "LIDL": "LIDL",
            "PROFIT": "PROFI",
            "PROFI": "PROFI",
            "KAUFLAND": "KAUFLAND",
            "KAUFLAN": "KAUFLAND",
            "AUCHAN": "AUCHAN",
            "PENNY": "PENNY",
            "JUMBO": "JUMBO",
            "PROFT": "PROFI",
            "LID": "LIDL"
        }

    def __formatDate(self, dateString, timeString):
        if dateString is None:
            return None

        dateList = dateString.strip().split("/")
        formattedString = ""

        try:
            if len(dateList) != 3:
                return None

            if not self.__checkIsInt(dateList[0]) or not self.__checkIsInt(dateList[1]) or not self.__checkIsInt(dateList[2]):
                return None

            formattedString += f"{dateList[2]}-{dateList[1]}-{dateList[0]}"

        except Exception:
            return None

        oldFormattedString = copy.deepcopy(formattedString)
        if timeString is not None:
            timeList = timeString.strip().split("-")

            try:
                if len(timeList) == 0:  # detected nothing
                    formattedString += "T00:00:00"
                    return formattedString

                if len(timeList) == 1:  # detected only the hour value
                    if not self.__checkIsInt(timeList[0]):
                        formattedString += f"T00:00:00"
                        return formattedString

                    formattedString += f"T{timeList[0]}:00:00"
                    return formattedString

                if len(timeList) == 2:  # detected the hour and the minute values
                    if not self.__checkIsInt(timeList[0]):
                        formattedString += f"T00:00:00"
                        return formattedString

                    if not self.__checkIsInt(timeList[1]):
                        formattedString += f"T{timeList[0]}:00:00"
                        return formattedString

                    formattedString += f"T{timeList[0]}:{timeList[1]}:00"
                    return formattedString

                if len(timeList) == 3:  # detected the hour, the minute and the second values
                    if not self.__checkIsInt(timeList[0]):
                        formattedString += f"T00:00:00"
                        return formattedString

                    if not self.__checkIsInt(timeList[1]):
                        formattedString += f"T{timeList[0]}:00:00"
                        return formattedString

                    if not self.__checkIsInt(timeList[2]):
                        formattedString += f"T{timeList[0]}:{timeList[1]}:00"
                        return formattedString

                    formattedString += f"T{timeList[0]}:{timeList[1]}:{timeList[2]}"
                    return formattedString

                return formattedString

            except Exception:
                return oldFormattedString

        if oldFormattedString == formattedString:   # if wasn't able to detect any time, it has to be added manually
            formattedString += f"T00:00:00"

        return formattedString

    def __checkIsInt(self, string):
        try:
            integer = int(string)
            return True
        except ValueError:
            return False
