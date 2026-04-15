i=1
while i<10:
    j=1
    while j<=i:
        message="%2d*%2d=%2d" % (j,i,i*j)
        print(message,"\t",end=" ")
        j+=1
    print("\n")
    i+=1