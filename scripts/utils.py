import numpy as np


def convertToGrayScale(image):
    height = len(image)
    width = len(image[0])
    converted = np.empty([height, width])

    for i in range(0, height):
        for j in range(0, width):
            converted[i][j] = int(image[i][j][0] * 0.299 + image[i][j][1] * 0.587 + image[i][j][2] * 0.114)

    return converted
