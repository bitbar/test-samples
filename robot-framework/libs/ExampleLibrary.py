# -*- coding: utf-8 -*-

class ExampleLibraryException(Exception):
    '''It is a good practice to throw library specific exceptions so
    that you know where the exception is comming'''
    pass

class ExampleLibrary(object):
    '''Libraries should be documented according to Robot Framework User Guide'''

    def library_keyword(self):
        '''Document keywords as well'''
        return True

