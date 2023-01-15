import json


class Receipt:
    def __init__(self, filePath, items, calculatedTotal, retailer):
        self.__filePath = filePath
        self.__items = items
        self.__calculatedTotal = calculatedTotal
        self.__detectedTotal = 0.0
        self.__retailer = retailer
        self.__receiptDate = None

    @property
    def detectedTotal(self):
        return self.__detectedTotal

    @detectedTotal.setter
    def detectedTotal(self, detectedTotal):
        self.__detectedTotal = detectedTotal

    @property
    def calculatedTotal(self):
        return self.__calculatedTotal

    @property
    def retailer(self):
        return self.__retailer

    @retailer.setter
    def retailer(self, retailer):
        self.__retailer = retailer

    @property
    def receiptDate(self):
        return self.__receiptDate

    @receiptDate.setter
    def receiptDate(self, receiptDate):
        self.__receiptDate = receiptDate

    def itemListToString(self):
        string = ""
        for item in self.__items:
            string += f"{item[0]}:{item[1]};"
        return string

    def toJson(self):
        dct = dict()
        dct["filePath"] = self.__filePath
        dct["items"] = self.itemListToString()
        dct["calculatedTotal"] = self.__calculatedTotal
        dct["detectedTotal"] = self.__detectedTotal
        dct["retailer"] = self.__retailer
        dct["receiptDate"] = self.__receiptDate

        return json.dumps(dct)
